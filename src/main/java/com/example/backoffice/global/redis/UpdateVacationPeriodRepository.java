package com.example.backoffice.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UpdateVacationPeriodRepository {

    private final ObjectMapper objectMapper;

    @Qualifier("redisTemplateForVacationPeriod")
    private final RedisTemplate<String, Object> redisTemplateForVacationPeriod;

    public UpdateVacationPeriodRepository (
            ObjectMapper objectMapper,
            @Qualifier("redisTemplateForVacationPeriod") RedisTemplate<String, Object> redisTemplateForVacationPeriod){
        this.objectMapper = objectMapper;
        this.redisTemplateForVacationPeriod = redisTemplateForVacationPeriod;
    }

    public <T> void saveMonthlyVacationPeriod(
            String key, Integer minutes, T value) throws JsonProcessingException{
        String valueString = null;
        try {
            valueString =
                    !(value instanceof String)
                            ? objectMapper.writeValueAsString(value)
                            : (String) value;
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }

        redisTemplateForVacationPeriod.opsForValue().set(key, objectMapper.writeValueAsString(value));
        redisTemplateForVacationPeriod.expire(key, minutes, TimeUnit.MINUTES);
    }

    public void deleteVacationPeriod(String key){
        redisTemplateForVacationPeriod.delete(key);
    }

    public boolean existsByKey(String key) {
        return redisTemplateForVacationPeriod.opsForValue().get(key) != null;
    }

    public <T> T getValueByKey(String key, Class<T> valueType) {
        Object value = redisTemplateForVacationPeriod.opsForValue().get(key);

        if (value == null) {
            return null;
        }

        try {
            if (value instanceof String) {
                // JSON 문자열로 간주하여 역직렬화
                return objectMapper.readValue((String) value, valueType);
            } else if (valueType.isInstance(value)) {
                return valueType.cast(value);
            } else {
                throw new IllegalArgumentException("The value retrieved from Redis is not compatible with the specified type.");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize value from Redis for key: " + key, e);
        }
    }
}
