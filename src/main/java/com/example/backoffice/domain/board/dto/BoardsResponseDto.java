package com.example.backoffice.domain.board.dto;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.image.entity.Images;
import com.example.backoffice.domain.like.entity.Likes;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
public class BoardsResponseDto {
    @Builder
    public static class ReadBoardListResponseDto {
        private String title;
        private String writer;
        private Integer likeCount;
        private Integer commentCount;
        private LocalDateTime modifiedAt;

        public static Page<ReadBoardListResponseDto> of(Page<Boards> boardPage){
            return boardPage.map(board -> {
                return ReadBoardListResponseDto.builder()
                        .title(board.getTitle())
                        .writer(board.getMember().getMemberName())
                        .likeCount(board.getLikeList().size())
                        .commentCount(board.getCommentList().size())
                        .modifiedAt(board.getModifiedAt())
                        .build();
            });
        }
    }

    @Builder
    public static class ReadBoardResponseDto {

        // Members.membername
        private String writer;
        private String title;
        private String content;
        // Like.count
        private Integer likeCount;
        // CommentList
        private List<Comments> commentList;
        private List<Images> imageList;

        public static ReadBoardResponseDto from(Boards board){
            return ReadBoardResponseDto.builder()
                    .likeCount(board.getLikeList().size())
                    .writer(board.getMember().getMemberName())
                    .content(board.getContent())
                    .commentList(board.getCommentList())
                    .imageList(board.getImageList())
                    .build();
        }
    }
    @Builder
    public static class CreateBoardResponseDto {
        private String writer;
        private String title;
        private String content;
        private List<Images> imageList;
        private List<Comments> commentList;
        private Integer likeCount;
        private LocalDateTime createdAt;
        public static CreateBoardResponseDto from(Boards board){
            return CreateBoardResponseDto.builder()
                    .writer(board.getMember().getMemberName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .imageList(board.getImageList())
                    .commentList(board.getCommentList())
                    .likeCount(board.getLikeList().size())
                    .createdAt(board.getCreatedAt())
                    .build();
        }
    }

    @Builder
    public static class UpdateBoardResponseDto {
        private String title;
        private String content;
        private String writer;
        private List<Comments> commentList;
        private Integer likeCount;
        private LocalDateTime createdAt;

        public static UpdateBoardResponseDto from(Boards board){
            return UpdateBoardResponseDto.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getMember().getMemberName())
                    .commentList(board.getCommentList())
                    .likeCount(board.getLikeCount())
                    .createdAt(board.getCreatedAt())
                    .build();
        }
    }
}
