package com.example.backoffice.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class RepliesResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadCommentRepliesDto {
        private Long replyId;
        private String replyWriter;
        private String replyContent;
        private Long likeCount;
        private Long unLikeCount;
        private LocalDateTime replyCreatedAt;
        private LocalDateTime replyModifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto {
        private String toMemberName;
        private String parentContent;
        private LocalDateTime parentCreatedAt;
        private String fromMemberName;
        private String childContent;
        private LocalDateTime childCreatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneDto {
        private String toMemberName;
        private String parentContent;
        private LocalDateTime parentCreatedAt;
        private LocalDateTime parentModifiedAt;
        private Long parentLikeCount;
        private Long parentUnLikeCount;
        private String fromMemberName;
        private String childContent;
        private LocalDateTime childCreatedAt;
        private LocalDateTime childModifiedAt;
        private Long childLikeCount;
        private Long childUnLikeCount;
    }
}
