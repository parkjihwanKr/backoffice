package com.example.backoffice.global.security;

import com.example.backoffice.global.exception.AuthenticationCustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
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

@Component
@RequiredArgsConstructor
@Slf4j(topic = "Logout process")
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtProvider jwtProvider;
    private final TokenRedisProvider tokenRedisProvider;
    // 3. accessToken, refreshToken 삭제
    // 4. logout 진행
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        log.info("logout 진행중...");
        try{
            // 1. accessToken, refreshToken 타당성 확인
            String tokenValue = jwtProvider.getJwtFromHeader(request);
            String authMemberName = jwtProvider.getUsernameFromToken(tokenValue);
            String redisTokenKey
                    = JwtProvider.REFRESH_TOKEN_HEADER+" : " +authMemberName;
            if(jwtProvider.validateToken(tokenValue).equals(JwtStatus.ACCESS) &&
                    tokenRedisProvider.getRefreshTokenValue(redisTokenKey).equals(null)){
                // 3. 보안을 위해 로그아웃하면 refreshToken 삭제
                tokenRedisProvider.deleteToken(redisTokenKey);
                log.info("logout success!");
            }
        }catch(Exception e){
            throw new AuthenticationCustomException(
                    GlobalExceptionCode.NOT_MATCHED_AUTHENTICATION
            );
        }
    }
}
