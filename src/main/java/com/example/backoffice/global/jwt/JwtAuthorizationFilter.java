package com.example.backoffice.global.jwt;

import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JwtCustomException;
import com.example.backoffice.global.redis.TokenRedisProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j(topic = "JWT 검증 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final TokenRedisProvider tokenRedisProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUrl = request.getRequestURI();

        if(requestUrl.equals("/api/v1/signup") || requestUrl.equals("/api/v1/login")){
            log.info("requestUrl : "+requestUrl);
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtProvider.getJwtFromHeader(request);

        log.info("doFilterInternal!!");
        log.info("accessToken : " + accessToken);
        if (StringUtils.hasText(accessToken)) {
            JwtStatus jwtStatus = jwtProvider.validateToken(accessToken);
            switch (jwtStatus) {
                case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                case ACCESS -> successValidatedToken(accessToken);
                case EXPIRED -> checkRefreshToken(request, response);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void successValidatedToken(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        String authName = authentication.getName();
        String refreshTokenKey = JwtProvider.REFRESH_TOKEN_HEADER + " : " + authName;
        // RefreshToken : name
        if (!tokenRedisProvider.existsByUsername(refreshTokenKey)) {
            return;
        }
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    // Access Token 기간이 만료시 Refresh Token을 체크해야 한다.
    private void checkRefreshToken(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String refreshToken = jwtProvider.getJwtFromHeader(request);
        String refreshTokenValue = jwtProvider.removeBearerPrefix(refreshToken);
        JwtStatus jwtStatus = jwtProvider.validateToken(refreshTokenValue);
        switch (jwtStatus) {
            case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
            case ACCESS -> makeNewAccessToken(refreshTokenValue, response);
            case EXPIRED -> throw new JwtCustomException(GlobalExceptionCode.UNAUTHORIZED_REFRESH_TOKEN_VALUE);
        }
    }

    // Refresh Token이 멀쩡할 시 새로 발급
    private void makeNewAccessToken(String tokenValue, HttpServletResponse response) throws UnsupportedEncodingException {
        Authentication authentication = jwtProvider.getAuthentication(tokenValue);
        if (tokenRedisProvider.existsByUsername(authentication.getName())) {
            String newAccessToken = jwtProvider.createToken(authentication.getName(), null)
                    .getAccessToken();
            String accessToken = URLEncoder.encode(newAccessToken, "utf-8").replaceAll("\\+", "%20");
            ResponseCookie accessCookie = ResponseCookie.from(JwtProvider.AUTHORIZATION_HEADER, accessToken)
                    .path("/")
                    .httpOnly(true)
                    .build();
            response.addHeader("Set-Cookie", accessCookie.toString());
        }
    }
}
