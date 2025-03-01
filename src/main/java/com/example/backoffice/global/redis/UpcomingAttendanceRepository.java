package com.example.backoffice.global.redis;

import com.example.backoffice.global.common.DateRange;
import com.example.backoffice.global.date.DateTimeUtils;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JsonCustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class UpcomingAttendanceRepository {
    // database 2 : cachedMemberAttendance
    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> redisTemplateForCached;

    // Long, DateRange
    public UpcomingAttendanceRepository(
            ObjectMapper objectMapper,
            @Qualifier("redisTemplateForCachedMemberAttendance") RedisTemplate<String, Object> redisTemplateForCached) {
        this.objectMapper = objectMapper;
        this.redisTemplateForCached = redisTemplateForCached;
    }

    public <T> void saveOne(Long memberId, DateRange value, String description) {
        String key = RedisProvider.MEMBER_ID_PREFIX + memberId + ", "+description;
        String valueString = serializeValue(value);

        Long ttl = DateTimeUtils.calculateMinutesFromTodayToEndDate(value.getEndDate());
        redisTemplateForCached.opsForValue().set(key, valueString, ttl, TimeUnit.MINUTES);
    }

    public String getRawValue(String key) {
        return (String) redisTemplateForCached.opsForValue().get(key);
    }

    // 키에 해당하는 value 조회
    public <T> T getValue(Long memberId, Class<T> valueType) {
        String key = RedisProvider.MEMBER_ID_PREFIX + memberId;
        String value = (String) redisTemplateForCached.opsForValue().get(key);

        if (Objects.isNull(value)) {
            return null; // 키가 없을 경우 null 반환
        }
        try {
            // JSON 문자열을 지정된 타입의 객체로 변환하여 반환
            return objectMapper.readValue(value, valueType);
        } catch (JsonProcessingException e) {
            throw new JsonCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
        }
    }

    public Map<String, String> getAllRawValues() {
        Set<String> keys = redisTemplateForCached.keys(
                RedisProvider.MEMBER_ID_PREFIX + "*");
        Map<String, String> allValues = new HashMap<>();

        if (keys != null) {
            for (String key : keys) {
                String value = getRawValue(key);
                allValues.put(key, value);
            }
        }

        return allValues;
    }

    // 토큰 삭제
    public void delete(String key) {
        redisTemplateForCached.delete(key);
    }

    // Key에서 memberId 추출
    public Long extractMemberIdFromKey(String key) {
        try {
            String memberIdPart = key.split(",")[0].split(":")[1].trim();
            return Long.valueOf(memberIdPart);
        } catch (Exception e) {
            return null;
        }
    }

    // Key에서 description 추출
    public String extractDescriptionFromKey(String key) {
        try {
            return key.split(",")[1].trim();
        } catch (Exception e) {
            return null;
        }
    }

    public Map<Long, String> getStartDatesByMemberIds(List<Long> memberIdList){
        // 모든 예정 근태 데이터 가져오기
        Map<String, String> upcomingAttendanceMap = getAllRawValues();
        Map<Long, String> memberStartDateMap = new HashMap<>();

        for (Map.Entry<String, String> entry : upcomingAttendanceMap.entrySet()) {
            String key = entry.getKey();  // 예: "memberId:3, 상하이 외근"
            String value = entry.getValue(); // 예: "{\"startDate\":\"2025-03-07 09:00:00\",\"endDate\":\"2025-03-14 18:00:00\"}"

            // JSON을 객체로 변환
            DateRange attendanceData = deserializeValue(value, DateRange.class);

            // key에서 memberId 추출
            Long memberId = extractMemberIdFromKey(key);

            // memberId가 리스트에 포함된 경우만 처리
            if (memberId != null && memberIdList.contains(memberId)) {
                memberStartDateMap.put(memberId, attendanceData.getStartDate().toString());
            }
        }

        return memberStartDateMap;
    }


    // JSON 직렬화
    private String serializeValue(DateRange value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JsonCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
        }
    }

    // JSON 역직렬화
    public <T> T deserializeValue(String value, Class<T> valueType) {
        try {
            return objectMapper.readValue(value, valueType);
        } catch (JsonProcessingException e) {
            throw new JsonCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
        }
    }
}
