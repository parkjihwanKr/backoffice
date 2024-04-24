package com.example.backoffice.global.jwt;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.jwt.dto.TokenDto;
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
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        // webSecurityConfig에서 잡아서 filter에서 login 처리하던 것을 변경
        // setFilterProcessesUrl("/api/v1/login");
    }

    // 지금 MemberController에서 잡아서 처리하기에 AuthenticationFilter가 필요가 없음
    // 밑에 존재하는 successfulAuthenitcation 안씀.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication()");
        log.info("request : "+request);
        try {
            MembersRequestDto.LoginMemberRequestDto requestDto
                    = new ObjectMapper().readValue(request.getInputStream(),
                    MembersRequestDto.LoginMemberRequestDto.class);

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

        TokenDto token = jwtProvider.createToken(username, role);
        response.setHeader(JwtProvider.AUTHORIZATION_HEADER, token.getAccessToken());
        response.setHeader(JwtProvider.REFRESH_TOKEN_HEADER, token.getRefreshToken());
        // response.setHeader(); 없을 때 넣어주는데, 중복된 토큰이 있으면 업데이트 해준다.
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
