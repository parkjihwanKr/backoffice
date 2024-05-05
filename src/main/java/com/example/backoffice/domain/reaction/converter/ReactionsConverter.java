package com.example.backoffice.domain.reaction.converter;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.entity.Emoji;
import com.example.backoffice.domain.reaction.entity.Reactions;

public class ReactionsConverter {

    public static Reactions toEntity(
            Members toMember, Members fromMember,
            Emoji emoji) {

        return Reactions.builder()
                .board(null)
                .comment(null)
                .member(toMember)
                .reactor(fromMember)
                .emoji(emoji)
                .build();
    }

    public static ReactionsResponseDto
            .CreateMemberReactionResponseDto toCreateMemberReactionDto(
                    Reactions reaction, String emoji){

        return ReactionsResponseDto.CreateMemberReactionResponseDto.builder()
                .fromMemberName(reaction.getMember().getMemberName())
                .toMemberName(reaction.getReactor().getMemberName())
                .emoji(emoji)
                .createdAt(reaction.getCreatedAt())
                .build();
    }
}
