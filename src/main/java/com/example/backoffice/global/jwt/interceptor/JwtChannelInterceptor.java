package com.example.backoffice.global.jwt.interceptor;

import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JwtCustomException;
import com.example.backoffice.global.jwt.JwtProvider;
import com.example.backoffice.global.jwt.JwtStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j(topic = "JwtChannelInterceptor logs")
@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    public JwtChannelInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    // 세션 유지 맵
    private static final Map<String, Authentication> sessionAuthenticationMap = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("JwtChannelInterceptor preSend invoked");
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);
        String sessionId = accessor.getSessionId();  // 현재 세션 ID 가져오기
        String destination = accessor.getDestination(); // destination 값 가져오기

        // destination 검증 로그 추가
        if (destination != null) {
            log.info("Destination: {}", destination);
        } else {
            log.info("No destination found for command: {}", accessor.getCommand());
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // CONNECT 요청 시 JWT 인증 처리
            String bearerToken = accessor.getFirstNativeHeader(JwtProvider.AUTHORIZATION_HEADER);
            String tokenValue = jwtProvider.removeBearerPrefix(bearerToken);

            if (tokenValue != null) {
                JwtStatus status = jwtProvider.validateToken(tokenValue);
                if (status == JwtStatus.ACCESS) {
                    Authentication auth = jwtProvider.getAuthentication(tokenValue);
                    if (auth != null) {
                        // 인증된 사용자 정보를 StompHeaderAccessor와 SecurityContextHolder에 저장
                        accessor.setUser(auth);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        log.info("Authenticated user set in SecurityContextHolder and accessor: " + auth.getName());

                        // 세션 ID와 인증 객체를 맵에 저장해 둠
                        sessionAuthenticationMap.put(sessionId, auth);
                    }
                }
            }
            // 세션 맵을 활용해서 해당 인증 객체가 유실되는 거 수정
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
            // SUBSCRIBE 또는 SEND 요청 시 인증 객체 확인
            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
            if (existingAuth == null) {
                // 세션 ID로 인증 객체를 가져옴
                existingAuth = sessionAuthenticationMap.get(sessionId);
                if (existingAuth != null) {
                    SecurityContextHolder.getContext().setAuthentication(existingAuth);
                    log.info("Restored authentication for session: " + sessionId + ", user: " + existingAuth.getName());
                } else {
                    log.error("No authenticated user found for SUBSCRIBE or SEND command.");
                    throw new JwtCustomException(GlobalExceptionCode.UNAUTHORIZED);
                }
            }
            log.info("existAuth : {}", existingAuth);
            log.info("securityContextHolder.getContext().getAuthentication : {}", SecurityContextHolder.getContext().getAuthentication());

            // 추가: destination 검증 로그
            if (destination != null) {
                log.info("Validating destination for {} command: {}", accessor.getCommand(), destination);
            }
        }

        log.info("JWT CHANNEL INTERCEPTOR {} MESSAGE : {}", accessor.getCommand(), message);
        return message;
    }
}
