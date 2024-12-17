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
    private static final String CHECK_REFRESH_TOKEN_URL = "/api/v1/refresh-token";
    // 필터를 무시할 api 또는 websocket
    private boolean isExcludedUrl(String requestUrl) {
        // 필터링을 건너뛰는 경로를 명시적으로 정의
        return requestUrl.startsWith("/ws")
                || requestUrl.equals("/api/v1/signup")
                || requestUrl.equals("/api/v1/login")
                || requestUrl.equals("/api/v1/check-available-memberName");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal!!");
        String requestUrl = request.getRequestURI();
        log.info("Request URL: " + requestUrl);

        // 특정 경로는 무시하고 진행
        if (isExcludedUrl(requestUrl)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(!requestUrl.equals(CHECK_REFRESH_TOKEN_URL)){
            String accessTokenValue = jwtProvider.getJwtFromHeader(request);
            JwtStatus jwtStatus = validateToken(accessTokenValue);
            switch (jwtStatus) {
                case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                case ACCESS -> successValidatedToken(accessTokenValue);
                case EXPIRED -> throw new JwtCustomException(GlobalExceptionCode.EXPIRED_JWT_TOKEN);
            }
        }else{
            String refreshTokenValue
                    = jwtProvider.getRefreshTokenFromHeader(request);
            JwtStatus jwtStatus = validateToken(refreshTokenValue);
            switch (jwtStatus) {
                case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                case ACCESS -> makeNewAccessToken(refreshTokenValue, response);
                case EXPIRED -> throw new JwtCustomException(GlobalExceptionCode.EXPIRED_JWT_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }

    private JwtStatus validateToken(String token){
        if (StringUtils.hasText(token)) {
            return jwtProvider.validateToken(token);
        }
        throw new JwtCustomException(GlobalExceptionCode.NOT_EXIST_JWT_STATUS);
    }

    private void successValidatedToken(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        String authName = authentication.getName();
        String refreshTokenKey = JwtProvider.REFRESH_TOKEN_HEADER + " : " + authName;
        // RefreshToken : name
        if (!tokenRedisProvider.existsByKey(refreshTokenKey)) {
            throw new JwtCustomException(GlobalExceptionCode.NOT_FOUND_REFRESH_TOKEN);
        }
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    // Refresh Token이 멀쩡할 시 새로 발급
    private void makeNewAccessToken(String refreshToken, HttpServletResponse response) throws UnsupportedEncodingException {
        // Refresh Token에서 인증 정보 추출
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        String username = authentication.getName();
        String redisKey = JwtProvider.REFRESH_TOKEN_HEADER+" : "+username;
        // Redis에 해당 Refresh Token이 존재하는지 검증
        if (tokenRedisProvider.existsByKey(redisKey)) {
            // 새 Access Token 생성
            String newAccessToken = jwtProvider.createToken(username, null).getAccessToken();
            String accessToken = URLEncoder.encode(newAccessToken, "utf-8").replaceAll("\\+", "%20");

            // Access Token을 Response Cookie에 설정
            ResponseCookie accessCookie = ResponseCookie.from(JwtProvider.ACCESS_TOKEN_HEADER, accessToken)
                    .path("/")
                    .httpOnly(true)
                    .maxAge(jwtProvider.getAccessTokenExpiration())
                    .build();
            response.addHeader("Set-Cookie", accessCookie.toString());

            // 원래 사용자 데이터도 함께 반환
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonResponse
                    = String.format(
                            "{\"accessToken\":\"%s\", \"username\":\"%s\"}",
                    newAccessToken, username);
            try{
                response.getWriter().write(jsonResponse);
            }catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }
    }
}
