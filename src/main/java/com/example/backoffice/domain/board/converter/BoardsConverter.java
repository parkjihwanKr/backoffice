package com.example.backoffice.domain.board.converter;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardsConverter {

    public static Boards toEntity(
            BoardsRequestDto.CreateBoardRequestDto requestDto, Members member){
        return Boards.builder()
                .member(member)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .likeCount(0L)
                .viewCount(0L)
                .build();
    }

    public static Page<BoardsResponseDto.ReadBoardListResponseDto> toReadDto(Page<Boards> boardPage){
        return boardPage.map(board -> {
            return BoardsResponseDto.ReadBoardListResponseDto.builder()
                    .title(board.getTitle())
                    .writer(board.getMember().getMemberName())
                    .content(board.getContent())
                    .likeCount(board.getLikeCount())
                    .viewCount(board.getViewCount())
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .build();
        });
    }

    public static BoardsResponseDto.ReadBoardResponseDto toReadOneDto(Boards board){

        List<String> fileUrls = board.getFileList().stream()
                .map(Files::getUrl)
                .collect(Collectors.toList());

        List<CommentsResponseDto.ReadBoardCommentResponseDto> commentList
                = new ArrayList<>();
        List<CommentsResponseDto.ReadCommentRepliesResponseDto> replyList
                = new ArrayList<>();

        for(int i = 0; i<board.getCommentList().size(); i++){
            for(int j = 0; j<board.getCommentList().get(i).getReplies().size(); j++){
                replyList.add(
                        CommentsResponseDto.ReadCommentRepliesResponseDto.builder()
                                .replyId(null)
                                .replyWriter(null)
                                .replyContent(null)
                                .replyCreatedAt(null)
                                .replyModifiedAt(null)
                                .build()
                );
            }

            commentList.add(
                    CommentsResponseDto.ReadBoardCommentResponseDto.builder()
                            .commentId(board.getCommentList().get(i).getId())
                            .commentWriter(board.getCommentList().get(i).getMember().getMemberName())
                            .commentContent(board.getCommentList().get(i).getContent())
                            .commentCreatedAt(board.getCommentList().get(i).getCreatedAt())
                            .commentModifiedAt(board.getCommentList().get(i).getModifiedAt())
                            .replyList(replyList)
                            .build()
            );
        }

        return BoardsResponseDto.ReadBoardResponseDto.builder()
                .title(board.getTitle())
                .writer(board.getMember().getMemberName())
                .content(board.getContent())
                .likeCount(board.getLikeList().size())
                .viewCount(board.getViewCount())
                .fileList(fileUrls)
                .commentList(commentList)
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }

    public static BoardsResponseDto.CreateBoardResponseDto toCreateDto(
            Boards board, List<String> fileUrlList){

        return BoardsResponseDto.CreateBoardResponseDto.builder()
                .writer(board.getMember().getMemberName())
                .title(board.getTitle())
                .content(board.getContent())
                .fileList(fileUrlList)
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static BoardsResponseDto.UpdateBoardResponseDto toUpdateDto(Boards board, List<String> fileUrlList){

        return BoardsResponseDto.UpdateBoardResponseDto.builder()
                .title(board.getTitle())
                .writer(board.getMember().getMemberName())
                .content(board.getContent())
                .fileList(fileUrlList)
                .commentList(board.getCommentList())
                .likeCount(board.getLikeCount())
                .viewCount(board.getViewCount())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }
}
