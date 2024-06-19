package com.example.backoffice.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentDto {
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCommentDto {
        private String content;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReplyDto {
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReplyDto {
        private String content;
    }
}
