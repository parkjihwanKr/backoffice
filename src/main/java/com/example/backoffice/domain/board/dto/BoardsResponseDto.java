package com.example.backoffice.domain.board.dto;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.image.entity.Images;
import lombok.Builder;

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

        public static List<ReadBoardListResponseDto> of(List<Boards> boardList){
            List<ReadBoardListResponseDto> responseDtoList= new ArrayList<>();

            // fix #2
            // 해당부분을 commentList.size()를 가지고 오는 것은 무거움
            for(int i = 0; i<boardList.size(); i++){
                ReadBoardListResponseDto responseDto = ReadBoardListResponseDto.builder()
                        .title(boardList.get(i).getTitle())
                        .writer(boardList.get(i).getMember().getMemberName())
                        .likeCount(boardList.get(i).getLikeList().size())
                        .commentCount(boardList.get(i).getCommentList().size())
                        .modifiedAt(boardList.get(i).getModifiedAt())
                        .build();
                responseDtoList.add(responseDto);
            }
            return responseDtoList;
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
}
