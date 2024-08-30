package com.example.backoffice.global.jwt;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JwtCustomException;
import com.example.backoffice.global.jwt.dto.TokenDto;
import com.example.backoffice.global.security.MemberDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    public static String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "refreshToken";
    public static final String AUTHORIZATION_KEY = "auth";
    // private static final long TOKEN_TIME = 60 * 60 * 1000L;
    public Long accessTokenExpiration = 1000 * 60 * 60L;
    public Long refreshTokenExpiration = 1000 * 60 * 60 * 24L;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final MemberDetailsServiceImpl memberDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenDto createToken(String username, MemberRole role) {
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpiration))
                .signWith(key, signatureAlgorithm)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpiration))
                .signWith(key, signatureAlgorithm)
                .compact();

        return TokenDto.of(accessToken, refreshToken);
    }

    public void setTokenForCookie(TokenDto tokenDto) {
        String accessToken = URLEncoder.encode(BEARER_PREFIX + tokenDto.getAccessToken(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String refreshToken = URLEncoder.encode(BEARER_PREFIX + tokenDto.getRefreshToken(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        makeTokenCookie(AUTHORIZATION_HEADER, accessToken);
        makeTokenCookie(REFRESH_TOKEN_HEADER, refreshToken);
    }

    private Cookie makeTokenCookie(String header, String value) {
        Cookie cookie = new Cookie(header, value);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public JwtStatus validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return JwtStatus.ACCESS;
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            log.error(e.getMessage());
            return JwtStatus.FAIL;
        } catch (SignatureException e) {
            log.error(e.getMessage());
            return JwtStatus.FAIL;
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            return JwtStatus.EXPIRED;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return JwtStatus.FAIL;
        } catch (Exception e) {
            log.error(e.getMessage());
            return JwtStatus.FAIL;
        }
    }

    public Claims getUserInfoFromToken(String token) {
        log.info("getUserInfoFromToken");
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // getUsernameFromRemovedJwtToken
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // getAccessTokenFromHeader
    public String getJwtFromHeader(HttpServletRequest req) {
        String accessToken = req.getHeader(AUTHORIZATION_HEADER);
        if (accessToken == null){
            String refreshToken = getRefreshTokenFromHeader(req);
        }
        log.info("Access Token : " + accessToken);
        return removeBearerPrefix(accessToken);
    }

    // getRefreshTokenFromHeader
    public String getRefreshTokenFromHeader(HttpServletRequest request){
        String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
        if (refreshToken == null){
            throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }
        return removeBearerPrefix(refreshToken);
    }

    public String removeBearerPrefix(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails user = memberDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }
}
