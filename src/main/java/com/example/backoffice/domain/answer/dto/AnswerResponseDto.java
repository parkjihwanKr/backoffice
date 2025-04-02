package com.example.backoffice.domain.answer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnswerResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "AnswerResponseDto.ReadOneDto",
            description = "답 하나 조회 응답 DTO")
    public static class ReadOneDto{
        private Long answerId;
        private String text;
        private Long orderNumber;
    }
}
