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
    public static class ReadBoardCommentsDto{
        private Long commentId;
        private String commentWriter;
        private String commentContent;
        private Long likeCount;
        private Long unLikeCount;
        private LocalDateTime commentCreatedAt;
        private LocalDateTime commentModifiedAt;
        private List<ReadCommentRepliesDto> replyList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentDto{
        private String writer;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCommentDto{
        private String writer;
        private String content;
        private Long likeCount;
        private Long unLikeCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

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
    public static class CreateReplyDto {
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
    public static class UpdateReplyDto {
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
