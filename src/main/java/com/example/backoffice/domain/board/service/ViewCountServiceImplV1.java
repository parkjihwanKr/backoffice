package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.redis.RedisProvider;
import com.example.backoffice.global.redis.ViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewCountServiceImplV1 implements ViewCountServiceV1{

    private final ViewCountRepository viewCountRedisProvider;

    @Override
    @Transactional
    public void incrementViewCount(Boards board, Members loginMember){
        String key
                = RedisProvider.BOARD_ID_PREFIX+ board.getId()
                + ":" + RedisProvider.MEMBER_ID_PREFIX+loginMember.getId();

        Long currentCount = viewCountRedisProvider.getViewCount(key);
        if (currentCount == null) {
            currentCount = 0L;
        }

        if (board.getMember().getId().equals(loginMember.getId())) {
            if (currentCount < 1) {
                viewCountRedisProvider.incrementViewCount(key);
            }
        } else {
            if (currentCount < 3) {
                viewCountRedisProvider.incrementViewCount(key);
            }
        }
    }

    @Override
    @Transactional
    public Long getTotalViewCountByBoardId(Long boardId){
        return viewCountRedisProvider.getTotalViewCountByBoardId(boardId);
    }

    @Override
    @Transactional
    public void deleteByBoardId(Long boardId) {
        viewCountRedisProvider.deleteByBoardId(boardId);
    }
}
