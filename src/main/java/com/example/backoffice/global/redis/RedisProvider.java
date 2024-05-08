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
    // database 0 : jwt token -> refreshToken
    private final RedisTemplate<String, Object> redisTemplateForToken;
    // database 1 : board -> viewCount
    private final RedisTemplate<String, Object> redisTemplateForViewCount;

    // login시 token 저장
    public <T> void saveToken(String key, Integer minutes, T value){
        String valueString = null;
        try{
            valueString = !(value instanceof String) ? objectMapper.writeValueAsString(value) : (String) value;
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }

        redisTemplateForToken.opsForValue().set(key, valueString);
        redisTemplateForToken.expire(key, minutes, TimeUnit.MINUTES);
    }

    // refreshToken 조회
    public <T> T getToken(String key, Class<T> valueType) {

        String value = (String) redisTemplateForToken.opsForValue().get(key);
        if (Objects.isNull(value)) {
            return null;
        }
        try {
            return objectMapper.readValue(value, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRefreshTokenValue(String key){
        // Long isExpiredRefreshToken = redisTemplateForToken.getExpire(key);
        return redisTemplateForToken.opsForValue().get(key).toString();
    }

    public <T> void saveViewCount(String key, T value){
        String valueString = null;
        try{
            valueString =
                    !(value instanceof String) ? objectMapper.writeValueAsString(value) : (String) value;
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }

        redisTemplateForToken.opsForValue().set(key, valueString);
    }

    // 토큰 삭제
    public void deleteToken(String key) {
        redisTemplateForToken.delete(key);
    }

    // username이 아니라 key로 찾아야함
    public boolean existsByUsername(String key) {
        String refreshToken = redisTemplateForToken.opsForValue().get(key).toString();
        return refreshToken != null && !refreshToken.isEmpty();
    }

    // 조회수 증가
    public Long incrementViewCount(String key) {
        return redisTemplateForViewCount.opsForValue().increment(key, 1);
    }

    // 조회수 감소
    public Long decreaseViewCount(String key){
        return redisTemplateForViewCount.opsForValue().decrement(key);
    }

    // 조회수 가져오기
    public Long getViewCount(String key){
        Object value = redisTemplateForViewCount.opsForValue().get(key);
        String serializeToJsonValue = serializeToJson(value);
        return value != null ? Long.parseLong(serializeToJsonValue) : null;
    }

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

