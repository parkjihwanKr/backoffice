package com.example.backoffice.domain.comment.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "CommentsResponseDto.ReadBoardCommentsDto",
            description = "게시글 댓글 조회 응답 DTO")
    public static class ReadBoardCommentsDto{
        private Long boardId;
        private Long commentId;
        private String author;
        private String content;
        private String authorDepartment;
        private String authorPosition;
        private Long likeCount;
        private LocalDateTime createdAt;
        private List<ReadCommentRepliesDto> replyList;
        private List<ReactionsResponseDto.ReadOneForCommentDto> reactionList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentsResponseDto.CreateCommentDto",
            description = "게시글 댓글 생성 응답 DTO")
    public static class CreateCommentDto{
        private Long commentId;
        private String author;
        private MemberDepartment authorDepartment;
        private MemberPosition authorPosition;
        private String content;
        private Long likeCount;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentsResponseDto.UpdateCommentDto",
            description = "게시글 댓글 수정 응답 DTO")
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
    @Schema(name = "CommentsResponseDto.ReadCommentRepliesDto",
            description = "댓글 답글 조회 응답 DTO")
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
        private List<ReactionsResponseDto.ReadOneForReplyDto> reactionList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentsResponseDto.CreateReplyDto ",
            description = "답글 생성 응답 DTO")
    public static class CreateReplyDto {
        private Long replyId;
        private String content;
        private String author;
        private Long likeCount;
        private LocalDateTime createdAt;
        private MemberDepartment authorDepartment;
        private MemberPosition authorPosition;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentsResponseDto.CreateReplyDto ",
            description = "답글 수정 응답 DTO")
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
