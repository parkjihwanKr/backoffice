package com.example.backoffice.global.jwt.service;

import com.example.backoffice.domain.member.converter.MembersConverter;
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
            default -> throw new JwtCustomException(GlobalExceptionCode.NOT_EXIST_JWT_STATUS);
        }
    }
    // access / refresh
    // fail, fail -> Exception
    // acc, acc -> return accValue
    // acc , ex -> return accValue, null, refreshCookie.toString()
    // ex , acc -> return accValue, accessCookie.toString(), null
    // ex , ex -> return accValue, accessCookie.toString(), refreshCookie.toString()
    private List<String> checkRefreshToken(JwtStatus accessStatus, String accessTokenValue, String refreshTokenValue){
        JwtStatus refreshStatus = jwtProvider.validateToken(refreshTokenValue);
        log.info("accessStatus : "+accessStatus);
        log.info("refreshStatus : "+refreshStatus);
        switch (refreshStatus){
            case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
            case ACCESS -> {
                if(accessStatus.equals(JwtStatus.ACCESS)){
                    return List.of(
                            accessTokenValue, refreshTokenValue);
                }
                if(accessStatus.equals(JwtStatus.EXPIRED)){
                    String newAccessTokenValue
                            = makeNewJwtToken(refreshTokenValue, true);
                    ResponseCookie accessCookie = cookieUtil.createCookie(
                            JwtProvider.ACCESS_TOKEN_HEADER,
                            newAccessTokenValue, jwtProvider.getAccessTokenExpiration());
                    return List.of(
                            newAccessTokenValue, refreshTokenValue,
                            accessCookie.toString(), "");
                }
                throw new JwtCustomException(GlobalExceptionCode.NOT_EXIST_JWT_STATUS);
            }
            case EXPIRED -> {
                if(accessStatus.equals(JwtStatus.ACCESS)){
                    String newRefreshTokenValue
                            = makeNewJwtToken(accessTokenValue, false);
                    ResponseCookie refreshCookie = cookieUtil.createCookie(
                            JwtProvider.ACCESS_TOKEN_HEADER,
                            newRefreshTokenValue, jwtProvider.getRefreshTokenExpiration());
                    return List.of(
                            accessTokenValue, newRefreshTokenValue,
                            "", refreshCookie.toString());
                }
                if(accessStatus.equals(JwtStatus.EXPIRED)){
                    return makeNewJwtTokenList(refreshTokenValue);
                }
                throw new JwtCustomException(GlobalExceptionCode.NOT_EXIST_JWT_STATUS);
            }
            default -> throw new JwtCustomException(GlobalExceptionCode.NOT_EXIST_JWT_STATUS);
        }
    }

    private List<String> makeNewJwtTokenList(String refreshTokenValue){
        Claims claim = getClaim(refreshTokenValue);
        String roleString = claim.get("auth", String.class);
        MemberRole role = MembersConverter.toRole(roleString);
        TokenDto tokenList = jwtProvider.createToken(claim.getSubject(), role);
        refreshTokenRepository.saveToken(
                RedisProvider.REFRESH_TOKEN_PREFIX,
                jwtProvider.getRefreshTokenExpiration().intValue() / (60 * 1000),
                tokenList.getRefreshToken());
        ResponseCookie accessCookie = cookieUtil.createCookie(
                JwtProvider.ACCESS_TOKEN_HEADER,
                tokenList.getAccessToken(), jwtProvider.getAccessTokenExpiration());
        ResponseCookie refreshCookie = cookieUtil.createCookie(
                JwtProvider.REFRESH_TOKEN_HEADER,
                tokenList.getRefreshToken(), jwtProvider.getRefreshTokenExpiration());
        return List.of(
                tokenList.getAccessToken(), tokenList.getRefreshToken(),
                accessCookie.toString(), refreshCookie.toString());
    }

    private String makeNewJwtToken(String tokenValue, boolean isAccessToken){
        Claims claim = getClaim(tokenValue);
        String roleString = claim.get("auth", String.class);
        MemberRole role = MembersConverter.toRole(roleString);
        TokenDto tokenList = jwtProvider.createToken(claim.getSubject(), role);
        if(isAccessToken){
            return tokenList.getAccessToken();
        }else{
            refreshTokenRepository.saveToken(
                    RedisProvider.REFRESH_TOKEN_PREFIX,
                    jwtProvider.getRefreshTokenExpiration().intValue() / (60 * 1000),
                    tokenList.getRefreshToken());
            return tokenList.getRefreshToken();
        }
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
