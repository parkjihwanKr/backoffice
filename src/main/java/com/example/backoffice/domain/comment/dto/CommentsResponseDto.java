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
        private List<RepliesResponseDto.ReadCommentRepliesDto> replyList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto{
        private String writer;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneDto{
        private String writer;
        private String content;
        private Long likeCount;
        private Long unLikeCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
