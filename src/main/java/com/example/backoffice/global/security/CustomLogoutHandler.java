package com.example.backoffice.global.security;

import com.example.backoffice.global.exception.AuthenticationCustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.jwt.CookieUtil;
import com.example.backoffice.global.jwt.JwtProvider;
import com.example.backoffice.global.jwt.JwtStatus;
import com.example.backoffice.global.redis.TokenRedisProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Enumeration;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "Logout process")
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtProvider jwtProvider;
    private final TokenRedisProvider tokenRedisProvider;
    private final CookieUtil cookieUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("logout 진행중...");

        String removedBearerPrefixAccessToken
                = cookieUtil.getCookieValue(request, "accessToken");
        String removedBearerPrefixRefreshToken
                = cookieUtil.getCookieValue(request, "refreshToken");

        try {
            // 1. accessToken, refreshToken 타당성 확인
            jwtProvider.validateToken(removedBearerPrefixAccessToken);
            jwtProvider.validateToken(removedBearerPrefixRefreshToken);

            // 2. accessToken의 username 확인
            String authMemberName
                    = jwtProvider.getUsernameFromToken(removedBearerPrefixAccessToken);

            // 3. redisTokenKey 확인
            String redisTokenKey
                    = JwtProvider.REFRESH_TOKEN_HEADER + " : " + authMemberName;

            // validateToken을 통해 ACCESS가 아니면 예외 발생하기에 따로 검증 하지 않음.
            if (tokenRedisProvider.getRefreshTokenValue(redisTokenKey) != null) {
                // 4. 보안을 위해 로그아웃하면 refreshToken 삭제
                tokenRedisProvider.deleteToken(redisTokenKey);
                log.info("delete refresh token success!");

                // 5. 쿠키 삭제
                cookieUtil.deleteCookie(response, "accessToken");
                cookieUtil.deleteCookie(response, "refreshToken");
                log.info("delete cookie in server success!");
            }
        } catch (Exception e) {
            throw new AuthenticationCustomException(
                    GlobalExceptionCode.NOT_MATCHED_AUTHENTICATION);
        }
    }
}
