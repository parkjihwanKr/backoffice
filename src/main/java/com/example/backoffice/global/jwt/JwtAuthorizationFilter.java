package com.example.backoffice.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.PrivilegedAction;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // OncePerRequestFilter
    // WebSecurityConfig http.addFilter()로 인하여 발생하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getTokenFromRequestCookie(req, jwtUtil.AUTHORIZATION_HEADER);
        if(StringUtils.hasText(accessToken)){
            String tokenValue = jwtUtil.substringToken(accessToken);
            JwtStatus jwtStatus = jwtUtil.validateToken(tokenValue);
            switch (jwtStatus) {
                case FAIL -> throw new IllegalArgumentException("sorry!");
                case ACCESS -> successValidatedToken(tokenValue);
                case EXPIRED -> log.info("test....");
            }
        }
        filterChain.doFilter(req, res);
    }

    private void successValidatedToken(String tokenValue){
        Authentication auth = jwtUtil.getAuthentication(tokenValue);
        if(refreshTokenRepository.existByUsername(auth.getName())){
            return;
        }
        // SecurityContext에 빈 객체를 넣어서 만듦
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }


}
