package com.example.backoffice.domain.reaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReactionsResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneForMemberDto {
        private Long reactionId;
        private String fromMemberName;
        private String toMemberName;
        private String emoji;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneForBoardDto {
        private Long reactionId;
        private String boardTitle;
        private String boardContent;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        private String fromMemberName;
        private String emoji;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneForCommentDto {
        private Long reactionId;
        private String commentContent;
        private Long likeCount;
        private Long unLikeCount;
        private String fromMemberName;
        private String emoji;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneForReplyDto {
        private Long reactionId;
        private String replyContent;
        private Long likeCount;
        private Long unLikeCount;
        private String fromMemberName;
        private String emoji;
    }
}
