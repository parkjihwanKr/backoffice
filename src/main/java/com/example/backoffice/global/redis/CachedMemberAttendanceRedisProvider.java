package com.example.backoffice.global.redis;

import com.example.backoffice.global.common.DateRange;
import com.example.backoffice.global.date.DateTimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class CachedMemberAttendanceRedisProvider {
    // database 2 : cachedMemberAttendance
    private final ObjectMapper objectMapper;
    private static final String keyPrefix = "memberId : ";

    private final RedisTemplate<String, Object> redisTemplateForCached;

    // Long, DateRange
    public CachedMemberAttendanceRedisProvider(
            ObjectMapper objectMapper,
            @Qualifier("redisTemplateForCachedMemberAttendance") RedisTemplate<String, Object> redisTemplateForCached) {
        this.objectMapper = objectMapper;
        this.redisTemplateForCached = redisTemplateForCached;
    }

    public <T> void saveOne(Long memberId, DateRange value) {
        String key = keyPrefix + memberId;
        String valueString = null;
        try {
            valueString = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }

        redisTemplateForCached.opsForValue().set(key, valueString);

        Long ttl = DateTimeUtils.calculateMinutesFromTodayToEndDate(value.getEndDate());
        redisTemplateForCached.expire(key, ttl, TimeUnit.MINUTES);
    }

    // 키에 해당하는 value 조회
    public <T> T getValue(Long memberId, Class<T> valueType) {
        String key = keyPrefix + memberId;
        String value = (String) redisTemplateForCached.opsForValue().get(key);

        if (Objects.isNull(value)) {
            return null; // 키가 없을 경우 null 반환
        }

        try {
            // JSON 문자열을 지정된 타입의 객체로 변환하여 반환
            return objectMapper.readValue(value, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize Redis value for key: " + key, e);
        }
    }

    // 토큰 삭제
    public void delete(String key) {
        redisTemplateForCached.delete(key);
    }

    public boolean existsByKey(Long memberId) {
        String key = keyPrefix + memberId;
        String refreshToken = redisTemplateForCached.opsForValue().get(key).toString();
        return refreshToken != null && !refreshToken.isEmpty();
    }
}
