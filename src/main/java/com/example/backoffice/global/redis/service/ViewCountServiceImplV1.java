package com.example.backoffice.global.redis.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.redis.repository.ViewCountRepository;
import com.example.backoffice.global.redis.utils.RedisProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ViewCountServiceImplV1 implements ViewCountServiceV1 {

    private final ViewCountRepository viewCountRepository;

    @Override
    @Transactional
    public void incrementViewCount(Boards board, Members loginMember){
        String key
                = RedisProvider.BOARD_ID_PREFIX+ board.getId()
                + ":" + RedisProvider.MEMBER_ID_PREFIX+loginMember.getId();

        String currentCountToString = viewCountRepository.getViewCount(key);
        long currentCount = parseLong(currentCountToString);

        if (board.getMember().getId().equals(loginMember.getId())) {
            if (currentCount < 1) {
                viewCountRepository.incrementViewCount(key);
            }
        } else {
            if (currentCount < 3) {
                viewCountRepository.incrementViewCount(key);
            }
        }
    }

    @Override
    @Transactional
    public Long getTotalViewCountByBoardId(Long boardId){
        Long viewCount = calculateViewCount(boardId);
        if(viewCount >= 51){
            return 51L;
        }
        return viewCount;
    }

    @Override
    @Transactional
    public Long clickTotalViewCountByBoardId(Long boardId){
        return calculateViewCount(boardId);
    }

    @CacheEvict(value = "viewCount", key = "'boardId:' + #boardId + ':memberId:' + #loginMemberId")
    @Transactional
    public void deleteByBoardId(Long boardId, Long loginMemberId) {
        viewCountRepository.deleteByBoardId(boardId);
    }

    private Long calculateViewCount(Long boardId){
        Set<String> stringSetByBoardId
                = viewCountRepository.getStringSetByBoardId(boardId);
        List<String> viewCounts
                = viewCountRepository.getViewCountsByKeys(stringSetByBoardId);
        return viewCounts.stream()
                .map(Long::parseLong)
                .reduce(0L, Long::sum);
    }
    private Long parseLong(String currentCountToString){
        return currentCountToString != null ? Long.parseLong(currentCountToString) : 0L;
    }
}
