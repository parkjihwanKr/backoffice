package com.example.backoffice.global.security;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.exception.AuthenticationCustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.jwt.JwtProvider;
import com.example.backoffice.global.jwt.dto.TokenDto;
import com.example.backoffice.global.redis.RedisProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtProvider jwtProvider;
    private final RedisProvider redisProvider;
    private final MemberDetailsServiceImpl memberDetailsService;

    public TokenDto generateAuthToken(String memberName) {
        // Authentication 객체 생성 및 SecurityContext에 설정
        Authentication authentication = createAuthentication(memberName);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        TokenDto tokenDto = jwtProvider.createToken(memberName, MemberRole.USER);
        jwtProvider.setTokenForCookie(tokenDto);

        // Redis에 Refresh Token 저장
        String refreshTokenKey = "refreshToken : " + memberName;
        redisProvider.saveToken(
                refreshTokenKey,
                Math.toIntExact(
                        jwtProvider.getRefreshTokenExpiration() / 1000),
                tokenDto.getRefreshToken()
        );

        return tokenDto;
    }

    public void setAuthentication(String username){
        Authentication authentication = createAuthentication(username);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    public String getCurrentMemberName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Authentication createAuthentication(String username){
        UserDetails userDetails = memberDetailsService.loadUserByUsername(username);
        // principal, credentials, authorities
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
