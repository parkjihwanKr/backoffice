package com.example.backoffice.global.config;

import com.example.backoffice.global.jwt.JwtAuthenticationFilter;
import com.example.backoffice.global.jwt.JwtAuthorizationFilter;
import com.example.backoffice.global.jwt.JwtProvider;
import com.example.backoffice.global.redis.TokenRedisProvider;
import com.example.backoffice.global.security.CustomLogoutHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    private final JwtProvider jwtProvider;
    private final TokenRedisProvider tokenRedisProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomLogoutHandler customLogoutHandler;

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
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider, tokenRedisProvider);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtProvider, tokenRedisProvider);
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
                            // local 환경 테스트를 위한 임시 허용
                            // 이거 계속 오류 -> 해결 방법 강구
                            configuration.addAllowedOriginPattern("*");
                            // configuration.setAllowedOrigins(Arrays.asList("http://example.com"));
                            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
                            configuration.setAllowedHeaders(Arrays.asList("Authorization", "RefreshToken", "Cache-Control", "Content-Type"));
                            configuration.setAllowCredentials(true);
                            return configuration;
                        })
                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/websocket").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/api/v1/logout").authenticated()
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/api/v1/goHome").permitAll()
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
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
        /*http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();*/
    }
}
