package com.example.backoffice.global.jwt;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.jwt.dto.ResponseTokenDto;
import com.example.backoffice.global.jwt.dto.TokenDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import com.example.backoffice.global.security.MemberDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    public static final long ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 60; // One Hour
    public static final long REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24; // One Day
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh";
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자(Convention)
    public static final String BEARER_PREFIX = "Bearer ";

    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberDetailsServiceImpl memberDetailsServiceImpl;

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    public void setup(){
        byte[] key = Decoders.BASE64URL.decode(secret);
        this.key = Keys.hmacShaKeyFor(key);
    }

    public JwtStatus validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return JwtStatus.ACCESS;
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            return JwtStatus.FAIL;
        } catch (SignatureException exception) {
            return JwtStatus.FAIL;
        } catch (ExpiredJwtException exception) {
            return JwtStatus.EXPIRED;
        } catch (IllegalArgumentException exception) {
            return JwtStatus.FAIL;
        } catch (Exception exception) {
            return JwtStatus.FAIL;
        }
    }

    // refreshToken은 유효하나 accessToken만 재발급이 필요할 때
    public String createAccessToken(Authentication auth){
        Date date = new Date();
        Date expireDate = new Date(date.getTime() + ACCESS_TOKEN_EXPIRED_TIME);

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(auth.getName())
                        .setIssuedAt(date)
                        .claim(AUTHORIZATION_KEY, MemberRole.USER)
                        .setExpiration(expireDate)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact();
    }

    public TokenDto createToken(Authentication auth){
        Date date = new Date();
        Date expireDate = new Date(date.getTime() + ACCESS_TOKEN_EXPIRED_TIME);

        String accessToken = createAccessToken(auth);

        String refreshToken = BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(auth.getName())
                        .setIssuedAt(date)
                        .claim(AUTHORIZATION_KEY, MemberRole.USER)
                        .setExpiration(expireDate)
                        .signWith(key, SignatureAlgorithm.ES512)
                        .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    public void saveRefreshToken(String username, String refreshToken){
        refreshTokenRepository.saveRefreshToken(username, refreshToken);
    }

    public void addJwtToCookie(ResponseTokenDto responseTokenDto, HttpServletResponse res){
        try{
            String accessToken = URLEncoder.encode(responseTokenDto.getAccessToken(), "utf-8").replaceAll("\\+", "%20");
            String refreshToken = URLEncoder.encode(responseTokenDto.getRefreshToken(), "utf-8").replaceAll("\\+", "%20");

            ResponseCookie accessCookie =
                    ResponseCookie.from(AUTHORIZATION_HEADER, accessToken)
                            //.domain(".air-dns.org")
                            .path("/")
                            //.httpOnly(true)
                            .build();

            ResponseCookie refreshCookie =
                    ResponseCookie.from(REFRESH_TOKEN_HEADER, refreshToken)
                            //.domain(".air-dns.org")
                            .path("/")
                            //.httpOnly(true)
                            .build();

            res.addHeader("Set-Cookie", accessCookie.toString());
            res.addHeader("Set-Cookie", refreshCookie.toString());
        }catch (UnsupportedEncodingException e) {
            log.error("Not Encoding");
        }
    }

    public Authentication getAuthentication(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails member = memberDetailsServiceImpl.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(member, null, authorities);

    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new NullPointerException("Not Found Token");
    }

    public String getTokenFromRequestCookie(HttpServletRequest req, String type) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(type)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
