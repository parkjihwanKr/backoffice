package com.example.backoffice.global.redis.service;

import com.example.backoffice.global.awss3.S3Util;
import com.example.backoffice.global.date.DateTimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisBackupService {

    private final ObjectMapper objectMapper;
    private final S3Util s3Util;

    @Qualifier("redisTemplateForToken")
    private final RedisTemplate<String, Object> redisTemplateForToken;

    @Qualifier("redisTemplateForViewCount")
    private final RedisTemplate<String, Object> redisTemplateForViewCount;

    @Qualifier("redisTemplateForCachedMemberAttendance")
    private final RedisTemplate<String, Object> redisTemplateForCachedMemberAttendance;

    @Qualifier("redisTemplateForVacationPeriod")
    private final RedisTemplate<String, Object> redisTemplateForVacationPeriod;

    public void backupAllRedisDataToS3() {
        try {
            // 1. 모든 Redis DB의 데이터를 가져오기
            Map<String, Object> redisData = Map.of(
                    "tokenData", fetchRedisData(redisTemplateForToken),
                    "viewCountData", fetchRedisData(redisTemplateForViewCount),
                    "attendanceData", fetchRedisData(redisTemplateForCachedMemberAttendance),
                    "vacationPeriodData", fetchRedisData(redisTemplateForVacationPeriod)
            );

            // 2. JSON 변환
            String jsonData = objectMapper.writeValueAsString(redisData);

            // 3. S3 파일명 설정
            String timestamp
                    = DateTimeUtils.getCurrentDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "backup/backup_" + timestamp + ".json";

            // 4. S3 업로드
            s3Util.uploadJsonToS3(jsonData, filename);

            log.info("Redis 백업 완료 → S3 저장: {}", filename);
        } catch (JsonProcessingException e) {
            log.error("Redis 데이터를 JSON 변환 중 오류 발생", e);
        }
    }

    /**
     * 특정 Redis DB에서 모든 데이터를 가져오는 메서드
     */
    private Map<String, Object> fetchRedisData(RedisTemplate<String, Object> redisTemplate) {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null || keys.isEmpty()) {
            return Map.of(); // 데이터 없음
        }

        return keys.stream()
                .collect(Collectors.toMap(key -> key, key -> redisTemplate.opsForValue().get(key)));
    }
}
