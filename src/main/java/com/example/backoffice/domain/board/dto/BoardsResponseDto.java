package com.example.backoffice.domain.board.dto;

import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BoardsResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadBoardListResponseDto {
        private String title;
        private String writer;
        private String content;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadBoardResponseDto {

        // Members.membername
        private String writer;
        private String title;
        private String content;
        // Like.count
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        // CommentList
        private List<CommentsResponseDto.ReadBoardCommentResponseDto> commentList;
        private List<String> fileList;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBoardResponseDto {
        private String writer;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private List<String> fileList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateBoardResponseDto {
        private String title;
        private String content;
        private String writer;
        private List<Comments> commentList;
        private List<String> fileList;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateImageBoardResponseDto {
        private String title;
        private String content;
        private String writer;
        private List<Comments> commentList;
        private Long likeCount;
        private LocalDateTime createdAt;
    }
}
