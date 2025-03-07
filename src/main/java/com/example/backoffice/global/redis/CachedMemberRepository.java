package com.example.backoffice.global.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CachedMemberRepository {

    private final ObjectMapper objectMapper;

    @Qualifier("redisTemplateFormCachedMember")
    private final RedisTemplate<String, Object> redisTemplateFormCachedMember;

    public CachedMemberRepository(
            ObjectMapper objectMapper,
            @Qualifier("redisTemplateFormCachedMember") RedisTemplate<String, Object> redisTemplateFormCachedMember){
        this.objectMapper = objectMapper;
        this.redisTemplateFormCachedMember = redisTemplateFormCachedMember;
    }


}
