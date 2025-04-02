package com.example.backoffice.global.redis.repository;

import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JwtCustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class RefreshTokenRepository {
    private final ObjectMapper objectMapper;

    // database 0 : jwt token -> refreshToken
    @Qualifier("redisTemplateForToken")
    private final RedisTemplate<String, Object> redisTemplateForToken;

    public RefreshTokenRepository(
            ObjectMapper objectMapper,
            @Qualifier("redisTemplateForToken") RedisTemplate<String, Object> redisTemplateForToken){
        this.objectMapper = objectMapper;
        this.redisTemplateForToken = redisTemplateForToken;
    }

    public <T> void saveToken(String key, Integer minutes, T value) {
        String valueString = null;
        try {
            valueString = !(value instanceof String) ? objectMapper.writeValueAsString(value) : (String) value;
        } catch (JsonProcessingException e) {
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

    public String getRefreshTokenValue(String key) {
        // Long isExpiredRefreshToken = redisTemplateForToken.getExpire(key);
        if(redisTemplateForToken.opsForValue().get(key) == null){
            throw new JwtCustomException(GlobalExceptionCode.TOKEN_VALUE_IS_NULL);
        }
        return redisTemplateForToken.opsForValue().get(key).toString();
    }

    // 토큰 삭제
    public void deleteToken(String key) {
        redisTemplateForToken.delete(key);
    }

    public boolean existsByKey(String key) {
        return redisTemplateForToken.opsForValue().get(key) != null;
    }
}
