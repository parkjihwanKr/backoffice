package com.example.backoffice.domain.comment.service;

import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.member.entity.Members;

public interface CommentsService {

    CommentsResponseDto.CreateCommentsResponseDto createComment(
            CommentsRequestDto.CreateCommentsRequestDto requestDto,
            Long boardId, Members member);
}
