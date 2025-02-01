package com.example.backoffice.domain.reaction.dto;

import com.example.backoffice.domain.reaction.entity.Emoji;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "ReactionsResponseDto.CreateOneForMemberDto",
            description = "멤버 리액션 생성 응답 DTO")
    public static class CreateOneForMemberDto {
        private Long reactionId;
        private String reactorName;
        private String toMemberName;
        private String emoji;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "ReactionsResponseDto.CreateOneForBoardDto",
            description = "게시글 리액션 생성 응답 DTO")
    public static class CreateOneForBoardDto {
        private Long reactionId;
        private String boardTitle;
        private String boardContent;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        private String reactorName;
        private String emoji;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "ReactionsResponseDto.ReadOneForBoardDto",
            description = "게시글 리액션 조회 응답 DTO")
    public static class ReadOneForBoardDto {
        private Long reactionId;
        private Long reactorId;
        private String reactorName;
        private Emoji emoji;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "ReactionsResponseDto.ReadOneForCommentDto",
            description = "댓글 리액션 조회 응답 DTO")
    public static class ReadOneForCommentDto {
        private Long commentId;
        private Long reactionId;
        private Long reactorId;
        private String reactorName;
        private Emoji emoji;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "ReactionsResponseDto.ReadOneForReplyDto",
            description = "대댓글 리액션 조회 응답 DTO")
    public static class ReadOneForReplyDto {
        private Long commentId;
        private Long replyId;
        private Long reactionId;
        private Long reactorId;
        private String reactorName;
        private Emoji emoji;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "ReactionsResponseDto.CreateOneForCommentDto",
            description = "댓글 리액션 생성 응답 DTO")
    public static class CreateOneForCommentDto {
        private Long reactionId;
        private String content;
        private Long likeCount;
        private String reactorName;
        private String emoji;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "ReactionsResponseDto.CreateOneForReplyDto",
            description = "대댓글 리액션 생성 응답 DTO")
    public static class CreateOneForReplyDto {
        private Long reactionId;
        private String content;
        private Long likeCount;
        private String reactorName;
        private String emoji;
    }
}
