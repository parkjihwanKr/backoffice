package com.example.backoffice.domain.comment.service;

import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;

public interface CommentsService {

    CommentsResponseDto.CreateCommentsResponseDto createComment(
            CommentsRequestDto.CreateCommentsRequestDto requestDto,
            Long boardId, Members member);

    CommentsResponseDto.UpdateCommentsResponseDto updateComment(
            Long boardId, Long commentId,
            CommentsRequestDto.UpdateCommentsRequestDto requestDto,
            Members member);

    void deleteComment(Long boardId, Long commentId, Members member);

    CommentsResponseDto.CreateReplyResponseDto createReply(
            Long boardId, Long commentId,
            CommentsRequestDto.CreateReplyRequestDto requestDto,
            Members member);

    CommentsResponseDto.UpdateReplyResponseDto updateReply(
            Long commentId, Long replyId,
            CommentsRequestDto.UpdateReplyRequestDto requestDto,
            Members member);

    void deleteReply(Long commentId, Long replyId, Members member);

    Comments findById(Long commentId);
}
