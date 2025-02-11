package com.example.backoffice.global.jwt.service;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JwtCustomException;
import com.example.backoffice.global.jwt.CookieUtil;
import com.example.backoffice.global.jwt.JwtProvider;
import com.example.backoffice.global.jwt.JwtStatus;
import com.example.backoffice.global.jwt.dto.AuthDto;
import com.example.backoffice.global.jwt.dto.TokenDto;
import com.example.backoffice.global.redis.RedisProvider;
import com.example.backoffice.global.redis.RefreshTokenRepository;
import com.example.backoffice.global.security.MemberDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "AuthService ")
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthDto checkAuth(Authentication authentication){
        MemberDetailsImpl memberDetails = (MemberDetailsImpl) authentication.getPrincipal();
        return AuthDto.of(
                memberDetails.getMembers().getId(), memberDetails.getMembers().getMemberName(),
                memberDetails.getMembers().getDepartment().getDepartment(),
                memberDetails.getMembers().getPosition().getPosition(),
                memberDetails.getMembers().getProfileImageUrl()
        );
    }

    public List<String> getToken(String accessTokenValue, String refreshTokenValue){
        if(accessTokenValue == null || refreshTokenValue == null){
            throw new JwtCustomException(GlobalExceptionCode.TOKEN_VALUE_IS_NULL);
        }

        JwtStatus accessStatus = jwtProvider.validateToken(accessTokenValue);
        switch (accessStatus) {
            case FAIL ->
                    throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
            case ACCESS, EXPIRED -> {
                return checkRefreshToken(accessStatus, accessTokenValue, refreshTokenValue);
            }
            default -> throw new JwtCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
        }
    }

    private List<String> checkRefreshToken(JwtStatus accessStatus, String accessTokenValue, String refreshTokenValue){
        JwtStatus refreshStatus = jwtProvider.validateToken(refreshTokenValue);
        switch (refreshStatus){
            case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
            case ACCESS -> {
                if(accessStatus.equals(JwtStatus.ACCESS)){
                    ResponseCookie accessCookie = cookieUtil.createCookie(
                            JwtProvider.ACCESS_TOKEN_HEADER, accessTokenValue, 0L);
                    ResponseCookie refreshCookie = cookieUtil.createCookie(
                            JwtProvider.REFRESH_TOKEN_HEADER, refreshTokenValue, 0L);
                    return List.of(
                            accessTokenValue,
                            accessCookie.toString(), refreshCookie.toString());
                }
                if(accessStatus.equals(JwtStatus.EXPIRED)){
                    String newAccessTokenValue = makeNewAccessToken(refreshTokenValue);
                    ResponseCookie accessCookie = cookieUtil.createCookie(
                            JwtProvider.ACCESS_TOKEN_HEADER, newAccessTokenValue, 0L);
                    ResponseCookie refreshCookie = cookieUtil.createCookie(
                            JwtProvider.REFRESH_TOKEN_HEADER, refreshTokenValue, 0L);
                    return List.of(
                            newAccessTokenValue,
                            accessCookie.toString(), refreshCookie.toString());
                }
                throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
            }
            case EXPIRED -> {
                return makeNewJwtTokenList(refreshTokenValue);
            }
            default -> throw new JwtCustomException(GlobalExceptionCode.MISSING_TOKEN);
        }
    }

    private List<String> makeNewJwtTokenList(String refreshTokenValue){
        Claims claim = getClaim(refreshTokenValue);
        TokenDto tokenList = jwtProvider.createToken(
                claim.getSubject(), claim.get("auth", MemberRole.class));
        ResponseCookie accessCookie = cookieUtil.createCookie(
                JwtProvider.ACCESS_TOKEN_HEADER, tokenList.getAccessToken(), 0L);
        ResponseCookie refreshCookie = cookieUtil.createCookie(
                JwtProvider.REFRESH_TOKEN_HEADER, tokenList.getRefreshToken(), 0L);
        return List.of(
                tokenList.getAccessToken(), accessCookie.toString(),
                refreshCookie.toString());
    }

    private String makeNewAccessToken(String refreshTokenValue){
        Claims claim = getClaim(refreshTokenValue);
        TokenDto token = jwtProvider.createAccessToken(
                claim.getSubject(), claim.get("auth", MemberRole.class));
        return token.getAccessToken();
    }

    private Claims getClaim(String refreshTokenValue){
        Claims claim = jwtProvider.getUserInfoFromToken(refreshTokenValue);
        String storedRefreshTokenValue
                = refreshTokenRepository.getRefreshTokenValue(
                RedisProvider.REFRESH_TOKEN_PREFIX+claim.getSubject());
        if(refreshTokenValue.equals(storedRefreshTokenValue)){
            throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }
        return claim;
    }
}
