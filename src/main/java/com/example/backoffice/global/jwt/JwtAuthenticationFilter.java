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
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;
    private final TokenRedisProvider tokenRedisProvider;
    private final CookieUtil cookieUtil;
    public JwtAuthenticationFilter(
            JwtProvider jwtProvider, TokenRedisProvider tokenRedisProvider,
            CookieUtil cookieUtil) {
        this.jwtProvider = jwtProvider;
        this.tokenRedisProvider = tokenRedisProvider;
        this.cookieUtil = cookieUtil;
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

        // Access Token Cookie settings
        ResponseCookie accessTokenCookie
                = cookieUtil.createCookie(
                        "accessToken", tokenDto.getAccessToken(),
                jwtProvider.getAccessTokenExpiration() / 1000);

        // Refresh Token Cookie settings
        ResponseCookie refreshTokenCookie
                = cookieUtil.createCookie(
                        "refreshToken", tokenDto.getRefreshToken(),
                jwtProvider.getRefreshTokenExpiration() / 1000);

        // add Response Header Cookie
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        // logging response Header
        log.info("Set-Cookie : "+accessTokenCookie.toString());

        // add Refresh token in redis
        tokenRedisProvider.saveToken(
                 refreshTokenCookie.getName()+ " : " + username,
                Math.toIntExact(
                        jwtProvider.getRefreshTokenExpiration() / 1000),
                tokenDto.getRefreshToken());

        log.info("AccessToken : " + tokenDto.getAccessToken());
        log.info("RefreshToken : " + tokenDto.getRefreshToken());

        // 쿠키 설정은 이미 되어 있으므로 생략
        // JSON 응답 보내기
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write("{\"status\":\"success\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 이전에 사용하던 방식
        /*response.setHeader(JwtProvider.AUTHORIZATION_HEADER, tokenDto.getAccessToken());
        String refreshToken = JwtProvider.REFRESH_TOKEN_HEADER;
        response.setHeader(refreshToken, tokenDto.getRefreshToken());
        tokenRedisProvider.saveToken(
                refreshToken + " : " + username,
                Math.toIntExact(
                        jwtProvider.getRefreshTokenExpiration() / 1000),
                tokenDto.getRefreshToken()
        );
        log.info("AccessToken : " + tokenDto.getAccessToken());
        log.info("RefreshToken : " + tokenDto.getRefreshToken());*/
        // response.setHeader(); 없을 때 넣어주는데, 중복된 토큰이 있으면 업데이트 해준다.
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
