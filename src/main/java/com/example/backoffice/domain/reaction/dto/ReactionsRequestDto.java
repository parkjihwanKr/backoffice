package com.example.backoffice.domain.reaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReactionsRequestDto",
        description = "리액션 생성 요청 DTO")
public class ReactionsRequestDto {
    private String emoji;
}
