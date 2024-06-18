package com.example.backoffice.domain.comment.service;

import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.dto.RepliesRequestDto;
import com.example.backoffice.domain.comment.dto.RepliesResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;

public interface CommentsServiceV1 {

    CommentsResponseDto.CreateOneDto createOneComment(
            CommentsRequestDto.CreateOneDto requestDto,
            Long boardId, Members member);

    CommentsResponseDto.UpdateOneDto updateOneComment(
            Long boardId, Long commentId,
            CommentsRequestDto.UpdateOneDto requestDto,
            Members member);

    void deleteOneComment(Long boardId, Long commentId, Members member);

    RepliesResponseDto.CreateOneDto createOneReply(
            Long boardId, Long commentId,
            RepliesRequestDto.CreateOneDto requestDto,
            Members member);

    RepliesResponseDto.UpdateOneDto updateOneReply(
            Long commentId, Long replyId,
            RepliesRequestDto.UpdateOneDto requestDto,
            Members member);

    void deleteOneReply(Long commentId, Long replyId, Members member);

    Comments findById(Long commentId);
}
