package com.example.backoffice.global.config;

import com.example.backoffice.global.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfigSecurity {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf(front) token 비활성화, 폼 로그인 비활성화,
        // http에서 제공하는 Authorization : base64 암호화 방식 변경 -> JWT 토큰 인증 방식으로 변경
        // spring boot에서 제공하는 session 방식 비활성화 -> JWT 토큰 인증 방식으로 변경
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                //.cors(corsConfigure -> corsConfigure.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((requests) -> requests
                        // 다 인증 없이 허용 -> 추후 변경 예정
                        // fix #1 webSecurityConfig : requestMatchers 변경
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        // http.addFilterBefore(jwtCustomExceptionFilter, AuthorizationFilter.class);
        return http.build();
    }
}
