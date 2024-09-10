package com.example.backoffice.domain.board.converter;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardsConverter {

    public static Boards toEntity(
            BoardsRequestDto.CreateOneDto requestDto, Members member, BoardType boardType){
        return Boards.builder()
                .member(member)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .isImportant(requestDto.getIsImportant())
                .boardType(boardType)
                .likeCount(0L)
                .unLikeCount(0L)
                .viewCount(0L)
                .build();
    }

    public static Page<BoardsResponseDto.ReadAllDto> toReadAllDto(Page<Boards> boardPage){
        return boardPage.map(board -> {
            return BoardsResponseDto.ReadAllDto.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .writer(board.getMember().getMemberName())
                    .content(board.getContent())
                    .likeCount(board.getLikeCount())
                    .unLikeCount(board.getUnLikeCount())
                    .viewCount(board.getViewCount())
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .boardType(board.getBoardType())
                    .build();
        });
    }

    public static BoardsResponseDto.ReadOneDto toReadOneDto(Boards board) {
        List<String> fileUrls = board.getFileList().stream()
                .map(Files::getUrl)
                .collect(Collectors.toList());

        List<CommentsResponseDto.ReadBoardCommentsDto> commentList = new ArrayList<>();

        // 댓글 리스트를 순회하면서 최상위 댓글과 대댓글을 구분하여 처리
        for (Comments comment : board.getCommentList()) {
            Long parentId = comment.getParent().getId();  // parent는 null이 아님
            Long commentId = comment.getId();

            // 최상위 댓글이 맞는지
            if (parentId.equals(commentId)) {
                List<CommentsResponseDto.ReadCommentRepliesDto> replyList = new ArrayList<>();

                // 해당 댓글의 대댓글 찾기
                for (Comments commentReply : board.getCommentList()) {
                    // 대댓글 리스트에 추가
                    if (commentReply.getParent().getId().equals(commentId) && !commentReply.getId().equals(commentId)) {
                        replyList.add(CommentsResponseDto.ReadCommentRepliesDto.builder()
                                .commentId(commentId)
                                .replyId(commentReply.getId())
                                .replyWriter(commentReply.getMember().getMemberName())
                                .replyContent(commentReply.getContent())
                                .likeCount(commentReply.getLikeCount())
                                .unLikeCount(commentReply.getUnLikeCount())
                                .replyCreatedAt(commentReply.getCreatedAt())
                                .replyModifiedAt(commentReply.getModifiedAt())
                                .build());
                    }
                }

                // 최상위 댓글을 댓글 리스트에 추가
                commentList.add(CommentsResponseDto.ReadBoardCommentsDto.builder()
                        .boardId(board.getId())
                        .commentId(commentId)
                        .commentWriter(comment.getMember().getMemberName())
                        .commentContent(comment.getContent())
                        .likeCount(comment.getLikeCount())
                        .unLikeCount(comment.getUnLikeCount())
                        .commentCreatedAt(comment.getCreatedAt())
                        .commentModifiedAt(comment.getModifiedAt())
                        .replyList(replyList)
                        .build());
            }
        }

        return BoardsResponseDto.ReadOneDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .writer(board.getMember().getMemberName())
                .content(board.getContent())
                .likeCount(board.getLikeCount())
                .unLikeCount(board.getUnLikeCount())
                .viewCount(board.getViewCount())
                .fileList(fileUrls)
                .commentList(commentList)
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .boardType(board.getBoardType())
                .build();
    }


    public static BoardsResponseDto.CreateOneDto toCreateOneDto(
            Boards board, List<String> fileUrlList){
        return BoardsResponseDto.CreateOneDto.builder()
                .boardId(board.getId())
                .writer(board.getMember().getMemberName())
                .title(board.getTitle())
                .content(board.getContent())
                .fileList(fileUrlList)
                .createdAt(board.getCreatedAt())
                .boardType(board.getBoardType())
                .build();
    }

    public static BoardsResponseDto.UpdateOneDto toUpdateOneDto(Boards board, List<String> fileUrlList){
        List<CommentsResponseDto.UpdateCommentDto> commentList = new ArrayList<>();
        if(!board.getCommentList().isEmpty()){
            for(int i = 0; i<board.getCommentList().size(); i++){
                commentList.add(
                        CommentsResponseDto.UpdateCommentDto.builder()
                                .content(board.getCommentList().get(i).getContent())
                                .build()
                );
            }
        }

        return BoardsResponseDto.UpdateOneDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .writer(board.getMember().getMemberName())
                .content(board.getContent())
                .fileList(fileUrlList)
                .commentList(board.getCommentList())
                .likeCount(board.getLikeCount())
                .unLikeCount(board.getUnLikeCount())
                .viewCount(board.getViewCount())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .boardType(board.getBoardType())
                .build();
    }
}
