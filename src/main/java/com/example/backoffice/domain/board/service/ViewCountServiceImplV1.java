package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.redis.utils.RedisProvider;
import com.example.backoffice.global.redis.repository.ViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewCountServiceImplV1 implements ViewCountServiceV1{

    private final ViewCountRepository viewCountRepository;

    @Override
    @Transactional
    public void incrementViewCount(Boards board, Members loginMember){
        String key
                = RedisProvider.BOARD_ID_PREFIX+ board.getId()
                + ":" + RedisProvider.MEMBER_ID_PREFIX+loginMember.getId();

        Long currentCount = viewCountRepository.getViewCount(key);
        if (currentCount == null) {
            currentCount = 0L;
        }

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
        return viewCountRepository.getTotalViewCountByBoardId(boardId);
    }

    @CacheEvict(value = "viewCount", key = "'boardId:' + #boardId + ':memberId:' + #loginMemberId")
    @Transactional
    public void deleteByBoardId(Long boardId, Long loginMemberId) {
        viewCountRepository.deleteByBoardId(boardId);
    }
}
