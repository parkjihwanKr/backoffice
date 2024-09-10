package com.example.backoffice.domain.board.dto;

import com.example.backoffice.domain.board.entity.BoardType;
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
    public static class ReadAllDto {
        private Long boardId;
        private String title;
        private String writer;
        private String content;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        private Boolean isImportant;
        private BoardType boardType;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadOneDto {
        // Members.membername
        private Long boardId;
        private String writer;
        private String title;
        private String content;
        private BoardType boardType;
        private Boolean isImportant;
        // Like.count
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        // CommentList
        private List<CommentsResponseDto.ReadBoardCommentsDto> commentList;
        private List<String> fileList;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto {
        private Long boardId;
        private String writer;
        private String title;
        private String content;
        private Boolean isImportant;
        private BoardType boardType;
        private LocalDateTime createdAt;
        private List<String> fileList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneDto {
        private Long boardId;
        private String title;
        private String content;
        private String writer;
        private List<Comments> commentList;
        private List<String> fileList;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        private Boolean isImportant;
        private BoardType boardType;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
