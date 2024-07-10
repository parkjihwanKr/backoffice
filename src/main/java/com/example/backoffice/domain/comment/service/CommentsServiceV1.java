package com.example.backoffice.domain.comment.service;

import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;

public interface CommentsServiceV1 {

    CommentsResponseDto.CreateCommentDto createComment(
            CommentsRequestDto.CreateCommentDto requestDto,
            Long boardId, Members member);

    CommentsResponseDto.UpdateCommentDto updateComment(
            Long boardId, Long commentId,
            CommentsRequestDto.UpdateCommentDto requestDto,
            Members member);

    void deleteComment(Long boardId, Long commentId, Members member);

    CommentsResponseDto.CreateReplyDto createReply(
            Long boardId, Long commentId,
            CommentsRequestDto.CreateReplyDto requestDto, Members member);

    CommentsResponseDto.UpdateReplyDto updateReply(
            Long commentId, Long replyId,
            CommentsRequestDto.UpdateReplyDto requestDto,
            Members member);

    void deleteReply(Long commentId, Long replyId, Members member);

    Comments findById(Long commentId);
}
