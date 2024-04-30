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
        private LocalDateTime modifiedAt;

        public static Page<ReadBoardListResponseDto> of(Page<Boards> boardPage){
            return boardPage.map(board -> {
                return ReadBoardListResponseDto.builder()
                        .title(board.getTitle())
                        .writer(board.getMember().getMemberName())
                        .likeCount(board.getLikeCount())
                        .viewCount(board.getViewCount())
                        .modifiedAt(board.getModifiedAt())
                        .build();
            });
        }
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

        public static ReadBoardResponseDto from(Boards board){

            List<String> fileUrls = board.getFileList().stream()
                    .map(Files::getUrl)
                    .collect(Collectors.toList());

            // Comments의 Comments까지 구현해야하기에 아직
            /*List<Comments> commentContents = board.getCommentList().stream()
                    .map(Comments::getContent)
                    .collect(Collectors.toList());*/

            return ReadBoardResponseDto.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getMember().getMemberName())
                    .likeCount(board.getLikeList().size())
                    .viewCount(board.getViewCount())
                    .commentList(board.getCommentList())
                    .fileList(fileUrls)
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .build();
        }
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
        public static CreateBoardResponseDto from(Boards board){
            return CreateBoardResponseDto.builder()
                    .writer(board.getMember().getMemberName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createdAt(board.getCreatedAt())
                    .build();
        }
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

        public static UpdateBoardResponseDto from(Boards board){
            return UpdateBoardResponseDto.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getMember().getMemberName())
                    .commentList(board.getCommentList())
                    .likeCount(0L)
                    .createdAt(board.getCreatedAt())
                    .build();
        }
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

        public static UpdateImageBoardResponseDto from(Boards board){
            return UpdateImageBoardResponseDto.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getMember().getMemberName())
                    .commentList(board.getCommentList())
                    .likeCount(0L)
                    .createdAt(board.getCreatedAt())
                    .build();
        }
    }
}
