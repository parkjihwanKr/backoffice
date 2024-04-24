package com.example.backoffice.global.jwt;

import com.example.backoffice.global.security.AuthenticationService;
import com.example.backoffice.global.security.MemberDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberDetailsServiceImpl memberDetailsService;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtProvider.getJwtFromHeader(req);
        log.info("token : "+token);
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            try {
                String username = jwtProvider.getUsernameFromToken(token);
                authenticationService.setAuthentication(username);
                log.info("username : "+username);
            } catch (Exception e) {
                log.error("Cannot set user authentication: {}", e.getMessage());
            }
        }
        filterChain.doFilter(req, res);
    }
}
