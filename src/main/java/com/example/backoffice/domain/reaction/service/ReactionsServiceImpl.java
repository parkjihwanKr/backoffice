package com.example.backoffice.domain.reaction.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.reaction.converter.ReactionsConverter;
import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.entity.Emoji;
import com.example.backoffice.domain.reaction.entity.Reactions;
import com.example.backoffice.domain.reaction.exception.ReactionsCustomException;
import com.example.backoffice.domain.reaction.exception.ReactionsExceptionCode;
import com.example.backoffice.domain.reaction.repository.ReactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionsServiceImpl implements ReactionsService{

    private final ReactionsRepository reactionsRepository;
    private final MembersService membersService;
    @Override
    @Transactional
    public ReactionsResponseDto.CreateMemberReactionResponseDto createMemberReaction
            (Long toMemberId, Members fromMember,
             ReactionsRequestDto.CreateMemberReactionsRequestDto requestDto){
        Members toMember = membersService.isMatchedLoginMember(toMemberId, fromMember.getId());

        Emoji emoji = isMatchedEmoji(requestDto);

        Reactions reaction = ReactionsConverter.toEntity(
                toMember, fromMember, emoji);
        reactionsRepository.save(reaction);

        toMember.addEmoji(reaction);

        return ReactionsConverter.toCreateMemberReactionDto(reaction, emoji.toString());
    }

    private Emoji isMatchedEmoji(ReactionsRequestDto.CreateMemberReactionsRequestDto requestDto){
        Emoji emoji;
        try {
            emoji = Emoji.valueOf(requestDto.getEmoji().toUpperCase()); // 클라이언트로부터 받은 문자열을 Emoji enum으로 변환
            return emoji;
        } catch (IllegalArgumentException e) {
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_MATCHED_EMOJI);
        }
    }
}
