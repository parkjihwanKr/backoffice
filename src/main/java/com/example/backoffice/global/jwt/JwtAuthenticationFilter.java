package com.example.backoffice.global.jwt;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.jwt.dto.TokenDto;
import com.example.backoffice.global.redis.RefreshTokenRepository;
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
    private final RefreshTokenRepository tokenRedisProvider;
    private final CookieUtil cookieUtil;
    public JwtAuthenticationFilter(
            JwtProvider jwtProvider, RefreshTokenRepository tokenRedisProvider,
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

        String origin = request.getHeader("Origin"); // 요청의 출처
        String referer = request.getHeader("Referer"); // 요청 발생 경로

        log.info("Request Origin: " + origin);
        log.info("Request Referer: " + referer);

        // 서버에서 수신한 요청 URL
        String serverRequestURL = request.getRequestURL().toString(); // 전체 URL
        String serverRequestURI = request.getRequestURI(); // URI 부분
        log.info("Server received request URL: " + serverRequestURL);
        log.info("Server received request URI: " + serverRequestURI);

        String username = ((MemberDetailsImpl) authResult.getPrincipal()).getUsername();
        MemberRole role = ((MemberDetailsImpl) authResult.getPrincipal()).getMembers().getRole();

        TokenDto tokenDto = jwtProvider.createToken(username, role);

        // Access Token Cookie settings
        ResponseCookie accessTokenCookie
                = cookieUtil.createCookie(
                        JwtProvider.ACCESS_TOKEN_HEADER, tokenDto.getAccessToken(),
                jwtProvider.getAccessTokenExpiration());
        response.addHeader("Set-Cookie", accessTokenCookie.toString());

        // Refresh Token Cookie settings
        String redisKey = JwtProvider.REFRESH_TOKEN_HEADER+" : "+username;

        // java.lang.NullPointerException: Cannot invoke "Object.toString()" because the return value of "org.springframework.data.redis.core.ValueOperations.get(Object)" is null
        // at com.example.backoffice.global.redis.TokenRedisProvider.existsByUsername(TokenRedisProvider.java:59) ~[main/:na]
        boolean existRefreshToken
                = tokenRedisProvider.existsByKey(redisKey);
        if(!existRefreshToken){
            ResponseCookie refreshTokenCookie
                    = cookieUtil.createCookie(
                    JwtProvider.REFRESH_TOKEN_HEADER, tokenDto.getRefreshToken(),
                    jwtProvider.getRefreshTokenExpiration());
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());

            System.out.println("refreshTokenCookie : "+refreshTokenCookie.toString());
            tokenRedisProvider.saveToken(
                    refreshTokenCookie.getName()+ " : " + username,
                    Math.toIntExact(
                            jwtProvider.getRefreshTokenExpiration()),
                    tokenDto.getRefreshToken());
        }else{
            String redisValue
                    = tokenRedisProvider.getRefreshTokenValue(redisKey);
            ResponseCookie refreshTokenCookie
                    = cookieUtil.createCookie(
                            JwtProvider.REFRESH_TOKEN_HEADER,
                    redisValue, jwtProvider.getRefreshTokenExpiration());
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        }

        // logging response Header
        log.info("Set-Cookie : "+accessTokenCookie.toString());

        log.info("AccessToken : " + tokenDto.getAccessToken());
        log.info("RefreshToken : " + tokenDto.getRefreshToken());

        // JSON 응답 보내기
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        try {
            response.getWriter().write("{\"status\":\"success\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
