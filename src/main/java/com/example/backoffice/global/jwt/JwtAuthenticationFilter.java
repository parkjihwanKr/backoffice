package com.example.backoffice.global.jwt;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.jwt.dto.TokenDto;
import com.example.backoffice.global.redis.TokenRedisProvider;
import com.example.backoffice.global.security.MemberDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;
    private final TokenRedisProvider tokenRedisProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, TokenRedisProvider tokenRedisProvider) {
        this.jwtProvider = jwtProvider;
        this.tokenRedisProvider = tokenRedisProvider;
        setFilterProcessesUrl("/api/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 로그아웃 로직일때 인증절차를 밟지 않고 LogoutFilter에서 끝냄
        log.info("attemptAuthentication()");
        try {
            MembersRequestDto.LoginDto requestDto
                    = new ObjectMapper().readValue(request.getInputStream(),
                    MembersRequestDto.LoginDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getMemberName(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        log.info("successfulAuthentication()");
        String username = ((MemberDetailsImpl) authResult.getPrincipal()).getUsername();
        MemberRole role = ((MemberDetailsImpl) authResult.getPrincipal()).getMembers().getRole();

        TokenDto tokenDto = jwtProvider.createToken(username, role);
        response.setHeader(JwtProvider.AUTHORIZATION_HEADER, tokenDto.getAccessToken());
        String refreshToken = JwtProvider.REFRESH_TOKEN_HEADER;
        response.setHeader(refreshToken, tokenDto.getRefreshToken());
        tokenRedisProvider.saveToken(
                refreshToken + " : " + username,
                Math.toIntExact(
                        jwtProvider.getRefreshTokenExpiration() / 1000),
                tokenDto.getRefreshToken()
        );
        log.info("AccessToken : " + tokenDto.getAccessToken());
        log.info("RefreshToken : " + tokenDto.getRefreshToken());
        // response.setHeader(); 없을 때 넣어주는데, 중복된 토큰이 있으면 업데이트 해준다.
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
