package com.example.backoffice.domain.board.dto;

import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.file.dto.FilesResponseDto;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
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
    public static class ReadOneDto {
        // Members.membername
        private Long boardId;
        private String writer;
        private String title;
        private String content;
        private MemberDepartment department;
        private MemberPosition position;
        private BoardType boardType;
        private Boolean isImportant;
        private Boolean isLocked;
        private String category;
        // Like.count
        private List<ReactionsResponseDto.ReadOneForBoardDto> reactionList;
        private Long likeCount;
        private Long unLikeCount;
        private Long viewCount;
        // CommentList
        private List<CommentsResponseDto.ReadBoardCommentsDto> commentList;
        private Long commentCount;
        // fileList
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
        private Boolean isLocked;
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
        private String category;
        private List<CommentsResponseDto.UpdateCommentDto> commentList;
        private List<String> fileList;
        private MemberDepartment department;
        private MemberPosition position;
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
