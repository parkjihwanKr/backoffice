package com.example.backoffice.domain.reaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReactionsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMemberReactionsRequestDto {
        private String emoji;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBoardReactionRequestDto {
        private String emoji;
    }
}
