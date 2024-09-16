package com.example.backoffice.domain.reaction.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;

import java.util.List;

public interface ReactionsServiceV1 {

    ReactionsResponseDto.CreateOneForMemberDto createOneForMember(
            Long toMemberId, Members fromMember,
            ReactionsRequestDto requestDto);

    void deleteOneForMember(
            Long toMemberId, Long reactionId, Members fromMember);

    List<ReactionsResponseDto.ReadOneForBoardDto> readAllForBoard(
            Long boardId);

    ReactionsResponseDto.CreateOneForBoardDto createOneForBoard(
            Long boardId, Members fromMember,
            ReactionsRequestDto requestDto
    );

    void deleteOneForBoard(
            Long boardId, Long reactionId, Members fromMember);

    ReactionsResponseDto.CreateOneForCommentDto createOneForComment(
            Long boardId, Long commentId, Members fromMember,
            ReactionsRequestDto requestDto);

    void deleteOneForComment(
            Long commentId, Long reactionId, Members froeMember);

    ReactionsResponseDto.CreateOneForReplyDto createOneForReply(
            Long commentId, Long reactionId, Members fromMember,
            ReactionsRequestDto requestDto);

    void deleteOneForReply(
            Long replyId, Long reactionId, Members fromMember);
}
