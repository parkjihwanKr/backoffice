package com.example.backoffice.domain.board.dto;

import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.file.dto.FilesResponseDto;
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

public class BoardsResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "BoardsResponseDto.ReadAllDto",
            description = "페이징된 요약된 게시글 조회 응답 DTO")
    public static class ReadAllDto {
        private Long boardId;
        private String title;
        private String author;
        private String content;
        private String categories;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        private Long commentCount;
        private Boolean isImportant;
        private Boolean isLocked;
        private BoardType boardType;
        private List<FilesResponseDto.ReadOneDto> fileList;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "BoardsResponseDto.ReadOneDto",
            description = "게시글 한 개 조회 응답 DTO")
    public static class ReadOneDto {
        private Long boardId;
        private String author;
        private String title;
        private String content;
        private MemberDepartment department;
        private MemberPosition position;
        private BoardType boardType;
        private Boolean isImportant;
        private Boolean isLocked;
        private String category;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        private Long commentCount;
        private List<String> fileList;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private List<CommentsResponseDto.ReadBoardCommentsDto> commentList;
        private List<ReactionsResponseDto.ReadOneForBoardDto> reactionList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "BoardsResponseDto.ReadSummaryOneDto",
            description = "요약된 게시글 한 개 조회 응답 DTO")
    public static class ReadSummarizedOneDto {
        private Long boardId;
        private String title;
        private String author;
        private BoardType boardType;
        private Long likeCount;
        private Long viewCount;
        private Integer commentCount;
        private Boolean isImportant;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "BoardsResponseDto.CreateOneDto",
            description = "게시글 작성 응답 DTO")
    public static class CreateOneDto {
        private Long boardId;
        private String author;
        private String title;
        private String content;
        private Boolean isImportant;
        private Boolean isLocked;
        private BoardType boardType;
        private LocalDateTime createdAt;
        private List<String> fileList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "BoardsResponseDto.UpdateOneDto",
            description = "게시글 수정 응답 DTO")
    public static class UpdateOneDto {
        private Long boardId;
        private String title;
        private String content;
        private String author;
        private String category;
        private List<CommentsResponseDto.UpdateCommentDto> commentList;
        private List<String> fileList;
        private MemberDepartment authorDepartment;
        private MemberPosition authorPosition;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        private Boolean isImportant;
        private Boolean isLocked;
        private BoardType boardType;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
