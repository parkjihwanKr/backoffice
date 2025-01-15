package com.example.backoffice.global.config;

import com.example.backoffice.global.jwt.interceptor.JwtChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // topic : 전체 알림, queue : 개인 알림
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 브라우저 CROS 이슈
        System.out.println("Web Socket endpoint registered");
        registry.addEndpoint("/ws")
                .setAllowedOrigins(
                        "http://localhost:3000", "http://localhost:8080",
                        "http://backofficefront.s3-website.ap-northeast-2.amazonaws.com",
                        "http://ec2-43-203-200-198.ap-northeast-2.compute.amazonaws.com")
                // ec2 서버도 추가해야함
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor); // JwtChannelInterceptor 등록
    }
}
