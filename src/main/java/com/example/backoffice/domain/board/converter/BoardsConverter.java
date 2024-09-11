package com.example.backoffice.domain.board.converter;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.BoardCategories;
import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.file.converter.FilesConverter;
import com.example.backoffice.domain.file.dto.FilesResponseDto;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.entity.Reactions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardsConverter {

    public static Boards toEntity(
            BoardsRequestDto.CreateOneDto requestDto, Members member,
            BoardCategories category){
        return Boards.builder()
                .member(member)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .isImportant(requestDto.getIsImportant())
                .boardType(BoardType.GENERAL)
                .department(member.getDepartment())
                .categories(category)
                .isLocked(false)
                .likeCount(0L)
                .unLikeCount(0L)
                .viewCount(0L)
                .build();
    }

    public static Boards toEntityForDepartment(
            BoardsRequestDto.CreateOneDto requestDto,
            Members member, MemberDepartment department){
        return Boards.builder()
                .member(member)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .isImportant(requestDto.getIsImportant())
                .isLocked(requestDto.getIsLocked())
                .boardType(BoardType.DEPARTMENT)
                .department(department)
                .likeCount(0L)
                .unLikeCount(0L)
                .viewCount(0L)
                .build();
    }

    public static BoardsResponseDto.ReadAllDto toReadAllDto(Boards board, Long commentCount) {
        // 게시글에 대한 File DTO 리스트 생성
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = board.getFileList().stream()
                .map(FilesConverter::toReadOneDto)
                .collect(Collectors.toList());

        // 게시글 DTO 생성
        return BoardsResponseDto.ReadAllDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .writer(board.getMember().getMemberName())
                .content(board.getContent())
                .isImportant(board.getIsImportant())
                .isLocked(board.getIsLocked())
                .likeCount(board.getLikeCount())
                .unLikeCount(board.getUnLikeCount())
                .viewCount(board.getViewCount())
                .commentCount(commentCount)  // 댓글 수 추가
                .fileList(fileResponseDtoList)
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .boardType(board.getBoardType())
                .build();
    }

    public static BoardsResponseDto.ReadOneDto toReadOneDto(Boards board, String loginMemberName) {
        List<String> fileUrls = board.getFileList().stream()
                .map(Files::getUrl)
                .collect(Collectors.toList());

        List<CommentsResponseDto.ReadBoardCommentsDto> commentList = new ArrayList<>();
        List<ReactionsResponseDto.ReadOneForBoardDto> reactionResponseDtoList = new ArrayList<>();
        List<Reactions> reactionList = board.getReactionList();

        for (Reactions reaction : reactionList){
            reactionResponseDtoList.add(
                    ReactionsResponseDto.ReadOneForBoardDto.builder()
                            .reactorName(loginMemberName)
                            .emoji(reaction.getEmoji())
                            .reactionId(reaction.getId())
                            .build());
        }
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
                        .commentCreatedAt(comment.getCreatedAt())
                        .replyList(replyList)
                        .commentWriterDepartment(comment.getMember().getDepartment().getDepartment())
                        .commentWriterPosition(comment.getMember().getPosition().getPosition())
                        .build());
            }
        }

        Long commentCount = (long) commentList.size();

        return BoardsResponseDto.ReadOneDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .writer(board.getMember().getMemberName())
                .department(board.getDepartment())
                .position(board.getMember().getPosition())
                .content(board.getContent())
                .likeCount(board.getLikeCount())
                .unLikeCount(board.getUnLikeCount())
                .viewCount(board.getViewCount())
                .isImportant(board.getIsImportant())
                .isLocked(board.getIsLocked())
                .reactionList(reactionResponseDtoList)
                .fileList(fileUrls)
                .category(board.getCategories().getLabel())
                .commentList(commentList)
                .commentCount(commentCount)
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

    public static BoardsResponseDto.UpdateOneDto toUpdateOneDto(
            String writerName, Boards board, List<String> fileUrlList){
        List<CommentsResponseDto.UpdateCommentDto> commentList = new ArrayList<>();
        if(!board.getCommentList().isEmpty()){
            for(int i = 0; i<board.getCommentList().size(); i++){
                commentList.add(
                        CommentsResponseDto.UpdateCommentDto.builder()
                                .commentId(board.getCommentList().get(i).getId())
                                .content(board.getCommentList().get(i).getContent())
                                .createdAt(board.getCommentList().get(i).getCreatedAt())
                                .writer(board.getCommentList().get(i).getMember().getMemberName())
                                .modifiedAt(board.getCommentList().get(i).getModifiedAt())
                                .likeCount(board.getCommentList().get(i).getLikeCount())
                                .unLikeCount(board.getCommentList().get(i).getUnLikeCount())
                                .build());
            }
        }

        return BoardsResponseDto.UpdateOneDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .writer(writerName)
                .content(board.getContent())
                .fileList(fileUrlList)
                .category(board.getCategories().getLabel())
                .commentList(commentList)
                .isImportant(board.getIsImportant())
                .isLocked(board.getIsLocked())
                .department(board.getDepartment())
                .position(board.getMember().getPosition())
                .likeCount(board.getLikeCount())
                .unLikeCount(board.getUnLikeCount())
                .viewCount(board.getViewCount())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .boardType(board.getBoardType())
                .build();
    }

    public static BoardCategories toCategories(String categoryName){
        System.out.println("categoryName : "+categoryName);
        for(BoardCategories categories : BoardCategories.values()){
            if(categories.getLabel().equalsIgnoreCase(categoryName)){
                return categories;
            }
        }
        throw new BoardsCustomException(BoardsExceptionCode.NOT_FOUND_BOARD_CATEGORIES);
    }
}
