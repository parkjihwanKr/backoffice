package com.example.backoffice.domain.reaction.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;

public interface ReactionsService {

    ReactionsResponseDto.CreateMemberReactionResponseDto createMemberReaction(
            Long memberId, Members member,
            ReactionsRequestDto requestDto);

    void deleteMemberReaction(
            Long toMemberId, Long reactionId, Members fromMember);

    ReactionsResponseDto.CreateBoardReactionResponseDto createBoardReaction(
            Long boardId, Members member,
            ReactionsRequestDto requestDto
    );

    void deleteBoardReaction(
            Long boardId, Long reactionId, Members member);

    ReactionsResponseDto.CreateCommentReactionResponseDto createCommentReaction(
            Long boardId, Long commentId, Members member,
            ReactionsRequestDto requestDto
    );

    void deleteCommentReaction(
            Long commentId, Long reactionId, Members member);
}
