package com.example.backoffice.domain.comment.service;

import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.comment.exception.CommentsCustomException;
import com.example.backoffice.domain.comment.exception.CommentsExceptionCode;
import com.example.backoffice.domain.member.entity.Members;

public interface CommentsServiceV1 {
    /**
     * 게시글 댓글 작성
     * @param requestDto{@link CommentsRequestDto.CreateCommentDto} 게시글 작성 요청 DTO
     * @param boardId : 댓글을 작성할 게시글 아이디
     * @param loginMember : 로그인 멤버
     * @return {@link CommentsResponseDto.CreateCommentDto} 게시글 작성 응답 DTO
     * @throws CommentsCustomException {@link CommentsExceptionCode#IS_EMPTY_COMMENT_CONTENT}
     * 댓글 요청 DTO에 작성란이 공란일 경우
     */
    CommentsResponseDto.CreateCommentDto createComment(
            CommentsRequestDto.CreateCommentDto requestDto,
            Long boardId, Members loginMember);

    /**
     * 게시글 댓글 수정
     * @param boardId : 수정하려는 댓글의 게시글 아이디
     * @param commentId : 수정하려는 댓글의 아이디
     * @param requestDto
     * {@link CommentsRequestDto.UpdateCommentDto} 댓글 수정 요청 DTO
     * @param loginMember : 로그인 멤버
     * @return {@link CommentsResponseDto.UpdateCommentDto}
     * @throws CommentsCustomException {@link CommentsExceptionCode#NOT_FOUND_COMMENT}
     * 해당 댓글이 존재하지 않는 경우
     * @throws CommentsCustomException {@link CommentsExceptionCode#IS_EMPTY_COMMENT_CONTENT}
     * 댓글 요청 DTO에 작성란이 공란일 경우
     * @throws CommentsCustomException {@link CommentsExceptionCode#NOT_MATCHED_BOARD_COMMENT}
     * 해당 댓글의 게시글 아이디와 요청 받은 boardId가 불일치하는 경우
     */
    CommentsResponseDto.UpdateCommentDto updateComment(
            Long boardId, Long commentId,
            CommentsRequestDto.UpdateCommentDto requestDto,
            Members loginMember);

    /**
     * 해당 게시글 댓글 삭제
     * @param boardId : 삭제하려는 댓글의 게시글 아이디
     * @param commentId : 삭제하려는 댓글의 아이디
     * @param loginMember : 로그인 멤버
     * @throws CommentsCustomException {@link CommentsExceptionCode#NOT_FOUND_COMMENT}
     * 삭제하려는 댓글이 존재하지 않은 경우
     * @throws CommentsCustomException {@link CommentsExceptionCode#NOT_MATCHED_BOARD_COMMENT}
     * 해당 댓글의 게시글 아이디와 요청 받은 boardId가 불일치하는 경우
     */
    void deleteComment(Long boardId, Long commentId, Members loginMember);

    /**
     * 해당하는 댓글의 대댓글 생성
     * @param boardId : 해당하는 댓글의 게시글 아이디
     * @param commentId : 해당하는 댓글의 아이디
     * @param requestDto {@link CommentsRequestDto.CreateCommentDto} 해당하는 대댓글 요청 DTO
     * @param loginMember : 로그인 멤버
     * @return {@link CommentsResponseDto.CreateReplyDto} 
     * 해당하는 댓글의 대댓글 생성 응답 DTO
     * @throws CommentsCustomException {@link CommentsExceptionCode#NOT_FOUND_COMMENT}
     * 해당 댓글이 존재하지 않는 경우
     * @throws CommentsCustomException {@link CommentsExceptionCode#IS_EMPTY_COMMENT_CONTENT}
     * 댓글 요청 DTO에 작성란이 공란일 경우
     * @throws CommentsCustomException {@link CommentsExceptionCode#NOT_MATCHED_BOARD_COMMENT}
     * 해당 댓글의 게시글 아이디와 요청 받은 boardId가 불일치하는 경우
     */
    CommentsResponseDto.CreateReplyDto createReply(
            Long boardId, Long commentId,
            CommentsRequestDto.CreateReplyDto requestDto, Members loginMember);

    /**
     * 해당하는 댓글의 대댓글의 수정
     * @param commentId : 수정하려는 대댓글의 부모인 댓글의 아이디
     * @param replyId : 대댓글 아이디 
     * @param requestDto {@link CommentsRequestDto.UpdateReplyDto} 수정하려는 대댓글의 요청 DTO
     * @param loginMember : 로그인 멤버
     * @return {@link CommentsResponseDto.UpdateReplyDto}
     * 수정하려는 대댓글의 응답 DTO
     * @throws CommentsCustomException {@link CommentsExceptionCode#NOT_FOUND_COMMENT}
     * 해당 대댓글이 존재하지 않는 경우
     * @throws CommentsCustomException {@link CommentsExceptionCode#IS_EMPTY_COMMENT_CONTENT}
     * 댓글 요청 DTO에 작성란이 공란일 경우
     * @throws CommentsCustomException {@link CommentsExceptionCode#IS_COMMENT}
     * 대댓글의 수정이 아닌 댓글의 수정이 이루어질려 할 때 
     * Request URL를 나누어놨음에도 
     * /comments/{commentId}/replies/{replyId}에 해당하는 URL로 접근
     */
    CommentsResponseDto.UpdateReplyDto updateReply(
            Long commentId, Long replyId,
            CommentsRequestDto.UpdateReplyDto requestDto,
            Members loginMember);

    /**
     * 해당하는 대댓글 삭제
     * @param commentId : 삭제하려는 대댓글의 댓글 아이디
     * @param replyId : 대댓글 아이디
     * @param loginMember : 로그인 멤버
     * @throws CommentsCustomException {@link CommentsExceptionCode#NOT_FOUND_COMMENT}
     * 해당 대댓글이 존재하지 않는 경우
     * @throws CommentsCustomException {@link CommentsExceptionCode#IS_COMMENT}
     * 대댓글의 수정이 아닌 댓글의 수정이 이루어질려 할 때
     * Request URL를 나누어놨음에도
     * /comments/{commentId}/replies/{replyId}에 해당하는 URL로 접근
     */
    void deleteReply(Long commentId, Long replyId, Members loginMember);

    /**
     * 해당하는 댓글 또는 대댓글 조회
     * @param commentId : 조회하려는 댓글 또는 대댓글 아이디
     * @return : 해당하는 댓글 또는 대댓글
     * @throws CommentsCustomException {@link CommentsExceptionCode#NOT_FOUND_COMMENT}
     * 해당 댓글 또는 대댓글이 존재하지 않는 경우
     */
    Comments findById(Long commentId);
}
