package com.example.backoffice.global.redis.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;

public interface ViewCountServiceV1 {

    /**
     * 해당하는 게시글을 찾아 조회수를 증가
     * @param board : 조회수를 올리려는 게시글(key)
     * @param loginMember : 로그인 멤버(key)
     * viewCount가 value를 의미
     */
    void incrementViewCount(Boards board, Members loginMember);

    /**
     * 게시글의 조회 수의 총합을 조회
     * @param boardId : 찾으려는 게시글 아이디
     * @return 게시글 아이디의 임계값 50까지의 총 조회수
     */
    Long getTotalViewCountByBoardId(Long boardId);

    /**
     * 게시글의 조회 수의 총합을 조회
     * @param boardId : 찾으려는 게시글 아이디
     * @return 게시글 아이디의 총 조회수
     */
    Long clickTotalViewCountByBoardId(Long boardId);

    /**
     * 게시글 삭제로 인하여 관련된 게시글 캐싱 삭제
     * @param boardId 삭제하려는 게시글 아이디
     */
    void deleteByBoardId(Long boardId, Long memberId);
}
