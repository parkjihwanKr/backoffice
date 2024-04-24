package com.example.backoffice.global.jwt;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.jwt.dto.TokenDto;
import com.example.backoffice.global.security.MemberDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Getter
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    public static String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String AUTHORIZATION_KEY = "auth";
    // private static final long TOKEN_TIME = 60 * 60 * 1000L;
    public Long accessTokenExpiration = 1000 * 60 * 60L;
    public Long refreshTokenExpiration = 1000 * 60 * 60 * 24L;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final MemberDetailsServiceImpl memberDetailsService;

    @Value("${JWT_SECRET}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenDto createToken(String username, MemberRole role){
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+accessTokenExpiration))
                .signWith(key, signatureAlgorithm)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+refreshTokenExpiration))
                .signWith(key, signatureAlgorithm)
                .compact();

        // 여기 안들어옴
        // log.info("accessToken : "+accessToken);
        // log.info("refreshToken : "+refreshToken);

        return TokenDto.of(accessToken, refreshToken);
    }

    public void setTokenForCookie(TokenDto tokenDto){
        String accessToken = URLEncoder.encode(BEARER_PREFIX + tokenDto.getAccessToken(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String refreshToken = URLEncoder.encode(BEARER_PREFIX + tokenDto.getRefreshToken(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        makeTokenCookie(AUTHORIZATION_HEADER, accessToken);
        makeTokenCookie(REFRESH_TOKEN_HEADER, refreshToken);
    }

    private Cookie makeTokenCookie(String header, String value){
        Cookie cookie = new Cookie(header, value);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        return cookie;
    }

    public boolean validateToken(String token) {
        log.info("token : "+ token);
        try {
            log.info("validateToken");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }
    public Claims getUserInfoFromToken(String token) {
        log.info("getUserInfoFromToken");
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getJwtFromHeader(HttpServletRequest req){
        String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
        log.info("bearer_token : "+bearerToken);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }
}
