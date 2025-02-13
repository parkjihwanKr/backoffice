package com.example.backoffice.global.config;

import com.example.backoffice.global.jwt.CookieUtil;
import com.example.backoffice.global.jwt.JwtAuthenticationFilter;
import com.example.backoffice.global.jwt.JwtAuthorizationFilter;
import com.example.backoffice.global.jwt.JwtProvider;
import com.example.backoffice.global.redis.RefreshTokenRepository;
import com.example.backoffice.global.security.CustomLogoutHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${server.port}")
    private String deploymentPort;

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository tokenRedisProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomLogoutHandler customLogoutHandler;
    private final CookieUtil cookieUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter
                = new JwtAuthenticationFilter(
                        jwtProvider, tokenRedisProvider, cookieUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(cookieUtil, jwtProvider, tokenRedisProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf(front) token 비활성화, 폼 로그인 비활성화,
        // http에서 제공하는 Authorization : base64 암호화 방식 변경 -> JWT 토큰 인증 방식으로 변경
        // spring boot에서 제공하는 session 방식 비활성화 -> JWT 토큰 인증 방식으로 변경
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration configuration = new CorsConfiguration();

                            // 테스트를 위한 임시로 접근 url을 모두 허용
                            configuration.setAllowedOriginPatterns(
                                    Arrays.asList(
                                            "http://localhost:3000", "http://localhost:8080",
                                            "http://api.baegobiseu.com",
                                            "https://api.baegobiseu.com", "https://baegobiseu.com"));
                            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
                            configuration.setAllowedHeaders(Arrays.asList("Authorization", "refreshToken", "Cache-Control", "Content-Type"));
                            configuration.setAllowCredentials(true);
                            configuration.setExposedHeaders(Arrays.asList("Authorization","Set-Cookie"));

                            return configuration;
                        })
                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/websocket", "/ws/**", "/wss/**",
                                "/api/v1/login","/api/v1/signup",
                                "/swagger-ui/**", "/v3/api-docs/**",
                                "/api/v1/check-available-memberName",
                                "/api/v1/health-check",
                                "/api/v1/access-token",
                                "https://baegobiseu.com/auth/login",
                                "https://baegobiseu.com/auth/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .logout((logout) -> logout
                        .logoutUrl("/api/v1/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .deleteCookies("remember-me")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
        // 필터 순서 조정
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
        /*http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();*/
    }
}
