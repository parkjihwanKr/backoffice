package com.example.backoffice.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "Redis Provider")
@Component
@RequiredArgsConstructor
public class RedisProvider {
    public static final String MEMBER_ID_PREFIX = "memberId:";
    public static final String BOARD_ID_PREFIX = "boardId:";
    public static final String REFRESH_TOKEN_PREFIX = "refreshToken : ";

    private final ObjectMapper objectMapper;

    // JSON 직렬화
    private String serializeToJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("JSON writing error", e);
            throw new RuntimeException("Error serializing object from JSON", e);
            // throw new JsonCustomException(GlobalExceptionCode.NOT_SERIALIZED_JSON);
        }
    }

    // JSON 역직렬화
    private <T> T deserializeFromJson(String json, Class<T> type) {
        try {
            return json != null ? objectMapper.readValue(json, type) : null;
        } catch (JsonProcessingException e) {
            log.error("JSON reading error", e);
            throw new RuntimeException("Error deserializing object from JSON", e);
        }
    }
}

