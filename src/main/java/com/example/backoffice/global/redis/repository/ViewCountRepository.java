package com.example.backoffice.global.redis.repository;

import com.example.backoffice.global.redis.utils.RedisProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ViewCountRepository {

    @Qualifier("redisTemplateForViewCount")
    private final RedisTemplate<String, String> redisTemplateForViewCount;

    public ViewCountRepository(
            @Qualifier("redisTemplateForViewCount") RedisTemplate<String, String> redisTemplateForViewCount){
        this.redisTemplateForViewCount = redisTemplateForViewCount;
    }

    // 조회수 증가
    public void incrementViewCount(String key) {
        redisTemplateForViewCount.opsForValue().increment(key, 1);
    }

    // 조회수 감소
    public Long decreaseViewCount(String key) {
        return redisTemplateForViewCount.opsForValue().decrement(key);
    }

    // 조회수 가져오기
    public String getViewCount(String key) {
        return redisTemplateForViewCount.opsForValue().get(key);
    }

    // 해당 게시글의 조회수 총 합 가지고 오기
    public Set<String> getStringSetByBoardId(Long boardId) {
        String keyPattern = getBoardKeyPattern(boardId);
        return scanKeys(keyPattern, 1000);
    }

    public List<String> getViewCountsByKeys(Set<String> keys) {
        if (keys.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> results = redisTemplateForViewCount.opsForValue().multiGet(keys);
        return results != null ? results.stream().filter(Objects::nonNull).collect(Collectors.toList()) : Collections.emptyList();
    }

    public void deleteByBoardId(Long boardId) {
        // 키 패턴 정의
        String keyPattern = getBoardKeyPattern(boardId);

        Set<String> keys = redisTemplateForViewCount.keys(keyPattern);

        if (keys != null && !keys.isEmpty()) {
            // 해당 키들을 삭제합니다.
            redisTemplateForViewCount.delete(keys);
        }
    }

    private String getBoardKeyPattern(Long domainId){
        return RedisProvider.BOARD_ID_PREFIX+domainId+":*";
    }

    private Set<String> scanKeys(String pattern, int count) {
        Set<String> keys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(count) // 한 번에 조회할 개수 설정
                .build();

        try (Cursor<byte[]> cursor = redisTemplateForViewCount
                .getConnectionFactory()
                .getConnection()
                .scan(options)) {

            while (cursor.hasNext()) {
                keys.add(new String(cursor.next())); // 키 저장
            }
        }
        return keys;
    }
}
