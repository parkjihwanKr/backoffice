package com.example.backoffice.global.jwt;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JwtCustomException;
import com.example.backoffice.global.redis.RefreshTokenRepository;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final CookieUtil cookieUtil;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 배포 상태 여부 체크
    @Value("${cookie.secure}")
    private boolean isProductionStatus;

    // 필터를 무시할 api 또는 websocket
    private boolean isExcludedUrl(String requestUrl) {
        // 필터링을 건너뛰는 경로를 명시적으로 정의
        return requestUrl.startsWith("/ws")
                || requestUrl.equals("/wss")
                || requestUrl.equals("/api/v1/signup")
                || requestUrl.equals("/api/v1/login")
                || requestUrl.equals("/api/v1/check-available-memberName")
                || requestUrl.startsWith("/swagger-ui")
                || requestUrl.startsWith("/v3/api-docs")
                || requestUrl.startsWith("/api/v1/health-check");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal!!");
        String requestUrl = request.getRequestURI();
        log.info("Request URL: " + requestUrl);

        // 특정 경로는 무시하고 진행
        if (isExcludedUrl(requestUrl)) {
            log.info("Excluded URL, skipping filter: {}", requestUrl);
            filterChain.doFilter(request, response);
            return;
        }

        if(!isProductionStatus){
            try {
                String accessTokenValue
                        = cookieUtil.getJwtTokenFromCookie(request, true);
                JwtStatus jwtStatus = validateToken(accessTokenValue);

                switch (jwtStatus) {
                    case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                    case ACCESS -> successValidatedToken(accessTokenValue);
                    case EXPIRED -> validateRefreshToken(response, accessTokenValue);
                }
            } catch (JwtCustomException e) {
                log.error("JWT Validation Error: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            }
        }else{
            try {
                String accessTokenValue
                        = cookieUtil.getJwtTokenFromCookie(request, true);
                // String accessTokenValue = jwtProvider.getJwtFromHeader(request);
                JwtStatus jwtStatus = validateToken(accessTokenValue);

                switch (jwtStatus) {
                    case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                    case ACCESS -> successValidatedToken(accessTokenValue);
                    case EXPIRED -> validateRefreshTokenInProduction(request, response);
                }
            } catch (JwtCustomException e) {
                log.error("JWT Validation Error: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
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
        if (!refreshTokenRepository.existsByKey(refreshTokenKey)) {
            throw new JwtCustomException(GlobalExceptionCode.NOT_FOUND_REFRESH_TOKEN);
        }
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private void validateRefreshToken(HttpServletResponse response, String accessTokenValue) throws UnsupportedEncodingException {
        Authentication authentication
                = jwtProvider.getAuthentication(accessTokenValue);
        String refreshTokenKey
                = JwtProvider.REFRESH_TOKEN_HEADER + " : " + authentication.getName();
        String refreshTokenValue
                = refreshTokenRepository.getRefreshTokenValue(refreshTokenKey);
        JwtStatus jwtStatus = validateToken(refreshTokenValue);
        switch (jwtStatus) {
            case ACCESS, EXPIRED -> makeNewAccessToken(refreshTokenValue, response, jwtStatus);
            case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }
    }

    // 액세스 토큰은 이미 만료된 상태
    private void validateRefreshTokenInProduction(
            HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String refreshTokenValue
                = cookieUtil.getJwtTokenFromCookie(request, false);
        JwtStatus jwtStatus = validateToken(refreshTokenValue);
        switch (jwtStatus) {
            case ACCESS -> makeNewAccessToken(refreshTokenValue, response, jwtStatus);
            case EXPIRED -> makeNewJwtToken(refreshTokenValue, response, jwtStatus);
            case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }
    }

    private void makeNewAccessToken(String refreshTokenValue, HttpServletResponse response, JwtStatus jwtStatus) throws UnsupportedEncodingException {
        // Refresh Token에서 인증 정보 추출
        Authentication authentication = jwtProvider.getAuthentication(refreshTokenValue);
        String username = authentication.getName();
        String redisKey = JwtProvider.REFRESH_TOKEN_HEADER+" : "+username;
        // Redis에 해당 Refresh Token이 존재하는지 검증
        if (refreshTokenRepository.existsByKey(redisKey)) {
            MemberRole role
                    = ((MemberDetailsImpl) authentication.getPrincipal()).getMembers().getRole();

            // 새 Access Token 생성
            String newAccessToken = jwtProvider.createToken(username, role).getAccessToken();
            String accessToken
                    = URLEncoder.encode(newAccessToken, "utf-8")
                    .replaceAll("\\+", "%20");

            ResponseCookie accessCookie
                    = cookieUtil.createCookie(
                            JwtProvider.ACCESS_TOKEN_HEADER, accessToken,
                    jwtProvider.getAccessTokenExpiration());
            response.addHeader("Set-Cookie", accessCookie.toString());

            String refreshCookie = refreshTokenRepository.getRefreshTokenValue(redisKey);
            response.addHeader("Set-Cookie", refreshCookie);

            log.info("accessCookie : "+accessCookie + " / refreshCookie : "+refreshCookie);
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

    private void makeNewJwtToken(String refreshTokenValue, HttpServletResponse response, JwtStatus jwtStatus) throws UnsupportedEncodingException {
        // Refresh Token에서 인증 정보 추출
        Authentication authentication = jwtProvider.getAuthentication(refreshTokenValue);
        String username = authentication.getName();
        String redisKey = JwtProvider.REFRESH_TOKEN_HEADER + " : " + username;
        // Redis에 해당 Refresh Token이 존재하는지 검증
        if (refreshTokenRepository.existsByKey(redisKey)) {
            MemberRole role
                    = ((MemberDetailsImpl) authentication.getPrincipal()).getMembers().getRole();

            // 새 Access Token 생성
            String newAccessToken = jwtProvider.createToken(username, role).getAccessToken();
            String accessToken = URLEncoder.encode(newAccessToken, "utf-8").replaceAll("\\+", "%20");

            // Access Token, RefreshToken을 ResponseCookie에 설정
            ResponseCookie accessCookie
                    = cookieUtil.createCookie(
                            JwtProvider.ACCESS_TOKEN_HEADER, accessToken,
                    jwtProvider.getAccessTokenExpiration());
            response.addHeader("Set-Cookie", accessCookie.toString());

            ResponseCookie refreshCookie
                    = cookieUtil.createCookie(
                            JwtProvider.REFRESH_TOKEN_HEADER, refreshTokenValue,
                    jwtProvider.getRefreshTokenExpiration());
            response.addHeader("Set-Cookie", refreshCookie.toString());

            refreshTokenRepository.deleteToken(redisKey);
            refreshTokenRepository.saveToken(
                    redisKey, Math.toIntExact(
                            jwtProvider.getRefreshTokenExpiration()),
                    refreshTokenValue);

            log.info("accessCookie : "+accessCookie + " / refreshCookie : "+refreshCookie);
            // 원래 사용자 데이터도 함께 반환
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonResponse
                    = String.format(
                    "{\"accessToken\":\"%s\", \"username\":\"%s\"}",
                    newAccessToken, username);
            try {
                response.getWriter().write(jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }
    }
}