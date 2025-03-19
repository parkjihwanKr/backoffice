package com.example.backoffice.global.redis.repository;

import com.example.backoffice.global.redis.utils.RedisProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
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
        Set<String> keys = redisTemplateForViewCount.keys(keyPattern);

        return keys == null ? Collections.emptySet() : keys;
    }

    public List<String> getViewCountsByKeys(Set<String> keys) {
        return keys.stream()
                .map(redisTemplateForViewCount.opsForValue()::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
}
