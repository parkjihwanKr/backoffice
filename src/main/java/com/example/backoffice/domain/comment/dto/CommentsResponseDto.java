package com.example.backoffice.domain.comment.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
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
        private Long boardId;
        private Long commentId;
        private String author;
        private String content;
        private String authorDepartment;
        private String authorPosition;
        private Boolean isLiked;
        private Long likeCount;
        private LocalDateTime createdAt;
        private List<ReadCommentRepliesDto> replyList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentDto{
        private Long commentId;
        private String author;
        private MemberDepartment authorDepartment;
        private MemberPosition authorPosition;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCommentDto{
        private Long commentId;
        private String author;
        private String content;
        private Long likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadCommentRepliesDto {
        private Long commentId;
        private Long replyId;
        private String author;
        private String content;
        private MemberPosition authorPosition;
        private MemberDepartment authorDepartment;
        private Long likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReplyDto {
        private Long replyId;
        private String content;
        private String author;
        private LocalDateTime createdAt;
        private MemberDepartment authorDepartment;
        private MemberPosition authorPosition;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReplyDto {
        private Long commentId;
        private Long replyId;
        private String author;
        private String content;
        private MemberDepartment authorDepartment;
        private MemberPosition authorPosition;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Long likeCount;
    }
}
