package com.example.backoffice.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "Redis Provider")
@Component
@RequiredArgsConstructor
public class RedisProvider {

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

