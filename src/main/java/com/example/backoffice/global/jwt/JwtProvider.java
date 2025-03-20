package com.example.backoffice.global.jwt;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JwtCustomException;
import com.example.backoffice.global.jwt.dto.TokenDto;
import com.example.backoffice.global.redis.repository.RefreshTokenRepository;
import com.example.backoffice.global.redis.utils.RedisProvider;
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
    public static final String ACCESS_TOKEN_HEADER = "accessToken";
    public static final String REFRESH_TOKEN_HEADER = "refreshToken";
    public static final String AUTHORIZATION_KEY = "auth";

    public Long accessTokenExpiration = 60 * 60 * 1000L;
    public Long refreshTokenExpiration = 24 * 60 * 60 * 1000L;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final MemberDetailsServiceImpl memberDetailsService;

    private final RefreshTokenRepository tokenRedisProvider;

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenDto createAccessToken(String username, MemberRole role) {
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpiration))
                .signWith(key, signatureAlgorithm)
                .compact();

        return TokenDto.of(accessToken);
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

    // removed Perfix(Bearer) accessToken || refreshToken
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
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired: {}", e.getMessage());
            return e.getClaims().getSubject(); // 만료된 토큰에서도 Subject 추출
        } catch (Exception e) {
            log.error("Error parsing token: {}", e.getMessage());
            throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }
    }

    // getAccessTokenFromHeader
    public String getJwtFromHeader(HttpServletRequest req) {
        log.info("httpServletRequest header : "+req.getHeader(AUTHORIZATION_HEADER));
        String accessToken = req.getHeader(AUTHORIZATION_HEADER);

        if (accessToken == null || !StringUtils.hasText(accessToken)) {
            log.error("Access Token is missing in the request header");
            throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }
        log.info("Access Token : " + accessToken);
        return removeBearerPrefix(accessToken);
    }

    public String getCookieFromHeader(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();

        return null;
    }

    // getRefreshTokenFromHeader
    public String getRefreshTokenFromHeader(HttpServletRequest request){
        String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
        // refresh token : Bearer tokenValue
        String refreshTokenValue = removeBearerPrefix(refreshToken);
        // ExpiredJwtException
        String memberName = getUsernameFromToken(refreshTokenValue);

        String redisKey = RedisProvider.REFRESH_TOKEN_PREFIX +memberName;
        String redisValue = tokenRedisProvider.getRefreshTokenValue(redisKey);

        if (refreshToken == null || redisValue == null){
            throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        }

        return refreshTokenValue;
    }

    public String removeBearerPrefix(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에서도 Claims를 추출
            claims = e.getClaims();
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails user = memberDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }
}
