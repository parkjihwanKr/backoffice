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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
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

    public List<String> getAccessToken(
            String accessToken, String refreshToken) throws UnsupportedEncodingException {
        if(accessToken == null){
            throw new JwtCustomException(GlobalExceptionCode.TOKEN_VALUE_IS_NULL);
        }
        String accessTokenValue
                = jwtProvider.removeBearerPrefix(accessToken);
        JwtStatus accessTokenStatus
                = jwtProvider.validateToken(accessTokenValue);
        String newAccessToken = null;
        switch (accessTokenStatus) {
            case ACCESS ->
                newAccessToken = makeNewAccessToken(accessToken);
            case EXPIRED -> {
                String name = getClaim(accessToken).getSubject();
                newAccessToken = checkRefreshToken(name, refreshToken);
            }
            case FAIL ->
                    throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }
        String accessCookie = cookieUtil.createCookie(
                "accessToken", newAccessToken,
                jwtProvider.getAccessTokenExpiration()).toString();
        return List.of(newAccessToken, accessCookie);
    }

    private String checkRefreshToken(
            String name, String refreshToken) throws UnsupportedEncodingException{
        if(refreshToken == null){
            throw new JwtCustomException(GlobalExceptionCode.TOKEN_VALUE_IS_NULL);
        }

        JwtStatus refreshTokenStatus = jwtProvider.validateToken(refreshToken);
        if (refreshTokenStatus == JwtStatus.FAIL) {
            throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }

        String savedRefreshToken
                = refreshTokenRepository.getRefreshTokenValue(
                        RedisProvider.REFRESH_TOKEN_PREFIX+name);
        if (!savedRefreshToken.equals(refreshToken)) {
            throw new JwtCustomException(GlobalExceptionCode.UNAUTHORIZED);
        }
        return makeNewAccessToken(refreshToken);
    }

    private Claims getClaim(String jwtToken){
        return jwtProvider.getUserInfoFromToken(jwtToken);
    }

    private String makeNewAccessToken(String jwtToken) throws UnsupportedEncodingException{
        String name = getClaim(jwtToken).getSubject();  // sub 값 가져오기
        String role = getClaim(jwtToken).get("auth", String.class);  // auth 값 가져오기
        MemberRole memberRole = MembersConverter.toRole(role);

        return URLEncoder.encode(
                jwtProvider.createToken(name, memberRole).getAccessToken(),
                "utf-8").replaceAll("\\+", "%20");
    }
}
