package com.example.backoffice.global.config;

import com.example.backoffice.global.jwt.interceptor.JwtChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.port}")
    private String deploymentPort;

    @Value("${cookie.secure}")
    private Boolean isSecure;

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
        // 로컬
        if(!isSecure){
            registry.addEndpoint("/ws")
                    .setAllowedOrigins(
                            "http://localhost:3000", "http://localhost:8080")
                    // ec2 서버도 추가해야함
                    .withSockJS();
        }else{
            // 배포
            registry.addEndpoint("/wss")
                    .setAllowedOrigins(
                            "https://baegobiseu.com", "https://api.baegobiseu.com")
                    // ec2 서버도 추가해야함
                    .withSockJS();
        }
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor); // JwtChannelInterceptor 등록
    }
}
