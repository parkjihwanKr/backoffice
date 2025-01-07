package com.example.backoffice.domain.reaction.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.exception.ReactionsCustomException;
import com.example.backoffice.domain.reaction.exception.ReactionsExceptionCode;

import java.util.List;

public interface ReactionsServiceV1 {

    /**
     * 멤버에게 '좋아요' 이모지 생성
     * @param toMemberId : '좋아요' 이모지를 받는 멤버
     * @param fromMember : '좋아요' 이모지를 주는 멤버
     * @param requestDto {@link ReactionsRequestDto}
     * 이모지 요청 DTO
     * @return {@link ReactionsResponseDto.CreateOneForMemberDto}
     * 멤버에게 '좋아요' 이모지 생성 응답 DTO
     */
    ReactionsResponseDto.CreateOneForMemberDto createOneForMember(
            Long toMemberId, Members fromMember,
            ReactionsRequestDto requestDto);

    /** 멤버에게 '좋아요' 이모지 생성 취소
     * @param toMemberId : '좋아요' 이모지를 취소 당하는 멤버
     * @param reactionId : '좋아요' 이모지 아이디
     * @param fromMember : '좋아요' 이모지를 취소하는 멤버
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_FOUND_REACTION}
     * 이모지를 취소하려는 멤버가 해당 멤버에게 이모지 '좋아요'를 주지 않은 경우
     */
    void deleteOneForMember(
            Long toMemberId, Long reactionId, Members fromMember);

    /**
     * 게시글의 모든 리액션 조회
     * @param boardId : 조회하려는 게시글 아이디
     * @return {@link List<ReactionsResponseDto.ReadOneForBoardDto>}
     * 해당하는 게시글의 모든 리액션 리스트 응답 DTO
     */
    List<ReactionsResponseDto.ReadOneForBoardDto> readAllForBoard(Long boardId);

    /**
     * 게시글의 댓글 하나의 모든 리액션 조회
     * @param boardId : 조회하려는 댓글의 게시글 아이디
     * @return {@link List<ReactionsResponseDto.ReadOneForCommentDto>}
     * 해당하는 게시글의 댓글 하나의 모든 리액션 리스트 응답 DTO
     */
    List<ReactionsResponseDto.ReadOneForCommentDto> readAllForComment(Long boardId);

    /**
     * 댓글의 답글 하나의 모든 리액션 조회
     * @param commentId : 조회하려는 댓글의 답글 게시글 아이디
     * @return {@link List<ReactionsResponseDto.ReadOneForCommentDto>}
     * 해당하는 댓글의 답글 하나의 모든 리액션 리스트 응답 DTO
     */
    List<ReactionsResponseDto.ReadOneForReplyDto> readAllForReply(Long commentId);

    /**
     * 게시글 하나의 '좋아요' 이모지 생성
     * @param boardId : 해당하는 게시글 아이디
     * @param fromMember : 이모지 '좋아요'를 주는 멤버
     * @param requestDto {@link ReactionsRequestDto} 리액션 요청 DTO
     * @return {@link ReactionsResponseDto.CreateOneForBoardDto}
     * 게시글 하나의 '좋아요' 이모지 생성 응답 DTO
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_ACCEPTED_EMOJI}
     * 서버에 존재하지만 해당 도메인에서는 사용되지 않은 이모티콘을 사용하는 경우
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_MATCHED_EMOJI}
     * 서버에 존재하지 않은 이모티콘을 직접 입력해서 사용하는 경우
     */
    ReactionsResponseDto.CreateOneForBoardDto createOneForBoard(
            Long boardId, Members fromMember,
            ReactionsRequestDto requestDto
    );

    /**
     * 게시글 하나의 '좋아요' 이모지 취소
     * @param boardId : 해당하는 게시글 아이디
     * @param reactionId : 해당하는 리액션 아이디
     * @param fromMember : '좋아요' 이모지를 준 멤버
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_FOUND_REACTION}
     * 이모지를 취소하려는 멤버가 해당 멤버에게 이모지 '좋아요'를 주지 않은 경우
     */
    void deleteOneForBoard(
            Long boardId, Long reactionId, Members fromMember);

    /**
     * 댓글 하나에 '좋아요' 이모지 생성
     * @param boardId : 해당하는 게시글 아이디
     * @param commentId : 해당하는 게시글의 댓글 아이디
     * @param fromMember : 이모지 '좋아요'를 주는 멤버
     * @param requestDto {@link ReactionsRequestDto} 리액션 요청 DTO
     * @return {@link ReactionsResponseDto.CreateOneForCommentDto}
     * 댓글 하나에 '좋아요' 이모지 생성 응답 DTO
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_ACCEPTED_EMOJI}
     * 서버에 존재하지만 해당 도메인에서는 사용되지 않은 이모티콘을 사용하는 경우
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_MATCHED_EMOJI}
     * 서버에 존재하지 않은 이모티콘을 직접 입력해서 사용하는 경우
     */
    ReactionsResponseDto.CreateOneForCommentDto createOneForComment(
            Long boardId, Long commentId, Members fromMember,
            ReactionsRequestDto requestDto);


    /**
     * 댓글 하나의 '좋아요' 이모지 취소
     * @param commentId : 해당하는 댓글 아이디
     * @param reactionId : 해당하는 리액션 아이디
     * @param fromMember : '좋아요' 이모지를 준 멤버
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_FOUND_REACTION}
     * 이모지를 취소하려는 멤버가 해당 멤버에게 이모지 '좋아요'를 주지 않은 경우
     */
    void deleteOneForComment(
            Long commentId, Long reactionId, Members fromMember);

    /**
     * 답글 하나에 '좋아요' 이모지 생성
     * @param commentId : 해당하는 댓글 아이디
     * @param reactionId : 해당하는 댓글의 답글 하나의 아이디
     * @param fromMember : 이모지 '좋아요'를 주는 멤버
     * @param requestDto {@link ReactionsRequestDto} 리액션 요청 DTO
     * @return {@link ReactionsResponseDto.CreateOneForReplyDto}
     * 답글 하나에 '좋아요' 이모지 생성 응답 DTO
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_ACCEPTED_EMOJI}
     * 서버에 존재하지만 해당 도메인에서는 사용되지 않은 이모티콘을 사용하는 경우
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_MATCHED_EMOJI}
     * 서버에 존재하지 않은 이모티콘을 직접 입력해서 사용하는 경우
     */
    ReactionsResponseDto.CreateOneForReplyDto createOneForReply(
            Long commentId, Long reactionId, Members fromMember,
            ReactionsRequestDto requestDto);

    /**
     * 답글 하나의 '좋아요' 이모지 취소
     * @param replyId : 해당하는 답글 아이디
     * @param reactionId : 해당하는 리액션 아이디
     * @param fromMember : '좋아요' 이모지를 준 멤버
     * @throws ReactionsCustomException {@link ReactionsExceptionCode#NOT_FOUND_REACTION}
     * 이모지를 취소하려는 멤버가 해당 멤버에게 이모지 '좋아요'를 주지 않은 경우
     */
    void deleteOneForReply(
            Long replyId, Long reactionId, Members fromMember);
}
