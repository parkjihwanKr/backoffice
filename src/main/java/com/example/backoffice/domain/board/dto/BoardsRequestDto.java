package com.example.backoffice.domain.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "BoardsRequestDto.CreateOneDto",
            description = "게시글 작성 요청 DTO")
    public static class CreateOneDto {
        private String title;
        private String content;
        private String category;
        private Boolean isImportant;
        private Boolean isLocked;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "BoardsRequestDto.UpdateOneDto",
            description = "게시글 수정 요청 DTO")
    public static class UpdateOneDto {
        private String title;
        private String content;
        private String category;
        private Boolean isImportant;
        private String department;
        private Boolean isLocked;
    }
}
