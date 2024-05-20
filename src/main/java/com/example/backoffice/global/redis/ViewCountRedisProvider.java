package com.example.backoffice.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewCountRedisProvider {
    private final ObjectMapper objectMapper;

    @Qualifier("redisTemplateForViewCount")
    private final RedisTemplate<String, Object> redisTemplateForViewCount;

    public <T> void saveViewCount(String key, T value){
        String valueString = null;
        try{
            valueString =
                    !(value instanceof String) ? objectMapper.writeValueAsString(value) : (String) value;
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }

        redisTemplateForViewCount.opsForValue().set(key, valueString);
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
            // log.error("JSON writing error", e);
            throw new RuntimeException("Error serializing object from JSON", e);
            // throw new JsonCustomException(GlobalExceptionCode.NOT_SERIALIZED_JSON);
        }
    }

    // JSON 역직렬화
    private <T> T deserializeFromJson(String json, Class<T> type) {
        try {
            return json != null ? objectMapper.readValue(json, type) : null;
        } catch (JsonProcessingException e) {
            // log.error("JSON reading error", e);
            throw new RuntimeException("Error deserializing object from JSON", e);
        }
    }
}
