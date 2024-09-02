package com.example.backoffice.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto {
        private String title;
        private String content;
        private Boolean isImportant;
        private String department;
        private Boolean isLocked;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneDto {
        private String title;
        private String content;
        private Boolean isImportant;
        private String department;
        private Boolean isLocked;
    }
}
