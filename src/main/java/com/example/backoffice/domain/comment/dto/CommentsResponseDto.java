package com.example.backoffice.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentsResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadBoardCommentResponseDto{
        private Long commentId;
        private String commentWriter;
        private String commentContent;
        private LocalDateTime commentCreatedAt;
        private LocalDateTime commentModifiedAt;
        private List<ReadCommentRepliesResponseDto> replyList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentsResponseDto{
        private String writer;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCommentsResponseDto{
        private String writer;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReplyResponseDto {
        private String fromMemberName;
        private String toMemberName;
        private String parentContent;
        private String childContent;
        private LocalDateTime parentCreatedAt;
        private LocalDateTime parentModifiedAt;
        private LocalDateTime childCreatedAt;
        private LocalDateTime childModifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadCommentRepliesResponseDto {
        private Long replyId;
        private String replyWriter;
        private String replyContent;
        private LocalDateTime replyCreatedAt;
        private LocalDateTime replyModifiedAt;
    }
}
