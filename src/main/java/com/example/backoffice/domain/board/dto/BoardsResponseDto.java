package com.example.backoffice.domain.board.dto;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.file.entity.Files;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BoardsResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadBoardListResponseDto {
        private String title;
        private String writer;
        private Long likeCount;
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
        private Integer likeCount;
        // CommentList
        private List<Comments> commentList;
        private List<String> fileList;
        private Long viewCount;
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
        private Long likeCount;
        private LocalDateTime createdAt;
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
