package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.redis.ViewCountRedisProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewCountServiceImplV1 implements ViewCountServiceV1{

    private final ViewCountRedisProvider viewCountRedisProvider;

    @Override
    @Transactional
    public void incrementViewCount(Boards board, Members loginMember){
        String key
                = ViewCountRedisProvider.boardIdPrefix+ board.getId()
                + ":" + ViewCountRedisProvider.memberIdPrefix+loginMember.getId();

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
