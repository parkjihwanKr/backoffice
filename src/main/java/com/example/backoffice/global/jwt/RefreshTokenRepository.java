package com.example.backoffice.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    // <K, V>
    private final RedisTemplate<String, String> redisTemplates;

    public void saveRefreshToken(String username, String refreshToken){
        // prefix : Bearer 제거
        String tokenValue = refreshToken.substring(7);
        long tokenTime = JwtUtil.REFRESH_TOKEN_EXPIRED_TIME;

        redisTemplates.opsForValue().set(username, tokenValue, tokenTime, TimeUnit.MICROSECONDS);
    }

    public boolean existByUsername(String username){
        String refreshToken = redisTemplates.opsForValue().get(username);
        if(refreshToken != null && refreshToken.isEmpty()){
            return true;
        }
        return false;
    }

    public void deleteRefreshToken(String username) {
        redisTemplates.delete(username);
    }
}
