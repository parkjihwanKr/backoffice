package com.example.backoffice.global.redis.repository;

import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JsonCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UpdateVacationPeriodRepository {

    private final ObjectMapper objectMapper;

    @Qualifier("redisTemplateForCacheData")
    private final RedisTemplate<String, Object> redisTemplateForCacheData;

    public UpdateVacationPeriodRepository (
            ObjectMapper objectMapper,
            @Qualifier("redisTemplateForCacheData") RedisTemplate<String, Object> redisTemplateForCacheData){
        this.objectMapper = objectMapper;
        this.redisTemplateForCacheData = redisTemplateForCacheData;
    }

    public <T> void save(String key, Integer minutes, T value) {
        try {
            redisTemplateForCacheData.opsForValue().set(key, value); // JSON 변환 제거
            redisTemplateForCacheData.expire(key, minutes, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new JsonCustomException(GlobalExceptionCode.NOT_SERIALIZED_JSON);
        }
    }

    public void delete(String key){
        redisTemplateForCacheData.delete(key);
    }

    public boolean existsByKey(String key) {
        return redisTemplateForCacheData.opsForValue().get(key) != null;
    }

    public <T> T getValueByKey(String key, Class<T> clazz) {
        Object cachedData = redisTemplateForCacheData.opsForValue().get(key);

        if (cachedData instanceof String) {
            try {
                return objectMapper.readValue((String) cachedData, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return clazz.cast(cachedData);
    }
}
