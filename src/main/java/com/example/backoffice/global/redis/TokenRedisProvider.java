package com.example.backoffice.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenRedisProvider {
    private final ObjectMapper objectMapper;
    // database 0 : jwt token -> refreshToken

    @Qualifier("redisTemplateForToken")
    private final RedisTemplate<String, Object> redisTemplateForToken;
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

    // 토큰 삭제
    public void deleteToken(String key) {
        redisTemplateForToken.delete(key);
    }

    public boolean existsByUsername(String key) {
        String refreshToken = redisTemplateForToken.opsForValue().get(key).toString();
        return refreshToken != null && !refreshToken.isEmpty();
    }
}
