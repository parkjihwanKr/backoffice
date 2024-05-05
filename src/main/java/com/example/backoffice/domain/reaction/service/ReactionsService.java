package com.example.backoffice.domain.reaction.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;

public interface ReactionsService {

    ReactionsResponseDto.CreateMemberReactionResponseDto createMemberReaction(
            Long memberId, Members member,
            ReactionsRequestDto.CreateMemberReactionsRequestDto requestDto);
}
