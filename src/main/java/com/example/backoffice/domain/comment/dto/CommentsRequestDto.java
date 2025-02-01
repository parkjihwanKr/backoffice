package com.example.backoffice.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentsRequestDto.CreateCommentDto",
            description = "댓글 생성 요청 DTO")
    public static class CreateCommentDto {
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentsRequestDto.UpdateCommentDto",
            description = "댓글 수정 요청 DTO")
    public static class UpdateCommentDto {
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentsRequestDto.CreateReplyDto",
            description = "댓글 생성 요청 DTO")
    public static class CreateReplyDto {
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentsRequestDto.UpdateReplyDto",
            description = "답글 수정 요청 DTO")
    public static class UpdateReplyDto {
        private String content;
    }
}
