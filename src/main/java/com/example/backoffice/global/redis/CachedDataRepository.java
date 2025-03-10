package com.example.backoffice.global.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CachedDataRepository {

    private final ObjectMapper objectMapper;

    @Qualifier("redisTemplateFormCachedData")
    private final RedisTemplate<String, Object> redisTemplateFormCachedData;

    public CachedDataRepository(
            ObjectMapper objectMapper,
            @Qualifier("redisTemplateFormCachedData") RedisTemplate<String, Object> redisTemplateFormCachedData){
        this.objectMapper = objectMapper;
        this.redisTemplateFormCachedData = redisTemplateFormCachedData;
    }


}
