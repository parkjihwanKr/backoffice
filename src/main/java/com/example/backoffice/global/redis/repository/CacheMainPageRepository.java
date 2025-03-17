package com.example.backoffice.global.redis.repository;

import com.example.backoffice.global.redis.utils.RedisProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CacheMainPageRepository {

    private final ObjectMapper objectMapper;

    @Qualifier("redisTemplateForCacheData")
    private final RedisTemplate<String, Object> redisTemplateForCacheData;

    // Long, DateRange
    public CacheMainPageRepository(
            ObjectMapper objectMapper,
            @Qualifier("redisTemplateForCacheData") RedisTemplate<String, Object> redisTemplateForCacheData) {
        this.objectMapper = objectMapper;
        this.redisTemplateForCacheData = redisTemplateForCacheData;
    }

    public <T> void save(
            String key, Integer minutes, T value) {

        redisTemplateForCacheData.opsForValue().set(key, value);
        redisTemplateForCacheData.expire(key, minutes, TimeUnit.MINUTES);
    }

    public void evict(List<Long> departmentMemberIdList){
        for(Long id : departmentMemberIdList){
            String key = RedisProvider.MAIN_PAGE_PREFIX+id;
            redisTemplateForCacheData.delete(key);
        }
    }
}
