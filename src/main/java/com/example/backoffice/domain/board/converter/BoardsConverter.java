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
                .build();
    }

    public static Boards toEntityForDepartment(
            BoardsRequestDto.CreateOneDto requestDto,
            Members member, MemberDepartment department, BoardCategories category){
        return Boards.builder()
                .member(member)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .isImportant(requestDto.getIsImportant())
                .isLocked(requestDto.getIsLocked())
                .boardType(BoardType.DEPARTMENT)
                .department(department)
                .categories(category)
                .likeCount(0L)
                .build();
    }

    public static BoardsResponseDto.ReadAllDto toReadAllDto(Boards board, Long commentCount, Long totalViewCount) {
        // 게시글에 대한 File DTO 리스트 생성
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = board.getFileList().stream()
                .map(FilesConverter::toReadOneDto)
                .collect(Collectors.toList());

        // 게시글 DTO 생성
        return BoardsResponseDto.ReadAllDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .author(board.getMember().getName())
                .content(board.getContent())
                .isImportant(board.getIsImportant())
                .isLocked(board.getIsLocked())
                .categories(board.getCategories().getLabel())
                .boardType(board.getBoardType())
                .likeCount(board.getLikeCount())
                .viewCount(totalViewCount)
                .commentCount(commentCount)  // 댓글 수 추가
                .fileList(fileResponseDtoList)
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }

    public static BoardsResponseDto.ReadOneDto toReadOneDto(
            Boards board, Long totalViewCount,
            List<ReactionsResponseDto.ReadOneForBoardDto> reactionBoardResponseDtoList,
            List<ReactionsResponseDto.ReadOneForCommentDto> reactionCommentResponseDtoList,
            List<ReactionsResponseDto.ReadOneForReplyDto> reactionReplyResponseDtoList) {
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
                                .author(commentReply.getMember().getName())
                                .content(commentReply.getContent())
                                .authorDepartment(commentReply.getMember().getDepartment())
                                .authorPosition(commentReply.getMember().getPosition())
                                .reactionList(
                                        reactionReplyResponseDtoList.stream()
                                                .filter(reaction -> reaction.getReplyId().equals(commentReply.getId()))
                                                .collect(Collectors.toList()))
                                .likeCount(commentReply.getLikeCount())
                                .createdAt(commentReply.getCreatedAt())
                                .modifiedAt(commentReply.getModifiedAt())
                                .build());
                    }
                }

                // 최상위 댓글을 댓글 리스트에 추가
                commentList.add(CommentsResponseDto.ReadBoardCommentsDto.builder()
                        .boardId(board.getId())
                        .commentId(commentId)
                        .author(comment.getMember().getName())
                        .content(comment.getContent())
                        .likeCount(comment.getLikeCount())
                        .createdAt(comment.getCreatedAt())
                        .reactionList(
                                // 댓글 ID에 맞는 reactionList를 필터링하여 적용
                                reactionCommentResponseDtoList.stream()
                                        .filter(reaction -> reaction.getCommentId().equals(commentId))  // 댓글 ID에 맞는 리액션 필터링
                                        .collect(Collectors.toList())  // 필터링된 리액션 리스트로 변환
                        )
                        .replyList(replyList)
                        .authorDepartment(comment.getMember().getDepartment().getDepartment())
                        .authorPosition(comment.getMember().getPosition().getPosition())
                        .build());
            }
        }

        Long commentCount = (long) commentList.size();

        return BoardsResponseDto.ReadOneDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .author(board.getMember().getName())
                .department(board.getDepartment())
                .position(board.getMember().getPosition())
                .content(board.getContent())
                .likeCount(board.getLikeCount())
                .isImportant(board.getIsImportant())
                .isLocked(board.getIsLocked())
                .reactionList(reactionBoardResponseDtoList)
                .fileList(fileUrls)
                .category(board.getCategories().getLabel())
                .commentList(commentList)
                .commentCount(commentCount)
                .viewCount(totalViewCount)
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }

    public static BoardsResponseDto.CreateOneDto toCreateOneDto(
            Boards board, List<String> fileUrlList, String loginMemberName){
        return BoardsResponseDto.CreateOneDto.builder()
                .boardId(board.getId())
                .author(loginMemberName)
                .title(board.getTitle())
                .content(board.getContent())
                .fileList(fileUrlList)
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static BoardsResponseDto.UpdateOneDto toUpdateOneDto(
            Boards board, List<String> fileUrlList){
        List<CommentsResponseDto.UpdateCommentDto> commentList = new ArrayList<>();
        if(!board.getCommentList().isEmpty()){
            for(int i = 0; i<board.getCommentList().size(); i++){
                commentList.add(
                        CommentsResponseDto.UpdateCommentDto.builder()
                                .commentId(board.getCommentList().get(i).getId())
                                .content(board.getCommentList().get(i).getContent())
                                .createdAt(board.getCommentList().get(i).getCreatedAt())
                                .author(board.getCommentList().get(i).getMember().getName())
                                .modifiedAt(board.getCommentList().get(i).getModifiedAt())
                                .likeCount(board.getCommentList().get(i).getLikeCount())
                                .build());
            }
        }

        return BoardsResponseDto.UpdateOneDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .author(board.getMember().getName())
                .content(board.getContent())
                .fileList(fileUrlList)
                .category(board.getCategories().getLabel())
                .commentList(commentList)
                .isImportant(board.getIsImportant())
                .isLocked(board.getIsLocked())
                .authorDepartment(board.getDepartment())
                .authorPosition(board.getMember().getPosition())
                .likeCount(board.getLikeCount())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }

    public static BoardsResponseDto.ReadSummaryOneDto toSummaryOneDto(
            Boards board, Long viewCount){
        return BoardsResponseDto.ReadSummaryOneDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .author(board.getMember().getMemberName())
                .boardType(board.getBoardType())
                .likeCount(board.getLikeCount())
                .viewCount(viewCount)
                .commentCount(board.getCommentList().size())
                .isImportant(board.getIsImportant())
                .build();
    }
    public static List<BoardsResponseDto.ReadSummaryOneDto> toReadSummaryListDto(
            List<Boards> boardList, List<Long> viewCountList) {
        if(boardList.size() == viewCountList.size()){
            List<BoardsResponseDto.ReadSummaryOneDto> responseDtoList = new ArrayList<>();
            for(int i = 0; i<boardList.size(); i++){
                responseDtoList.add(
                        BoardsConverter.toSummaryOneDto(boardList.get(i), viewCountList.get(i)));
            }
            return responseDtoList;
        }
        throw new BoardsCustomException(BoardsExceptionCode.NOT_EQUALS_LIST_SIZE);
    }

    public static BoardCategories toCategories(String categoryName){
        for(BoardCategories categories : BoardCategories.values()){
            if(categories.getLabel().equalsIgnoreCase(categoryName)){
                return categories;
            }
        }
        throw new BoardsCustomException(BoardsExceptionCode.NOT_FOUND_BOARD_CATEGORIES);
    }

    /*public static BoardType toBoardType(String boardTypeName){
        return switch (boardTypeName) {
            case "GENERAL"-> BoardType.GENERAL;
            case "DEPARTMENT" -> BoardType.DEPARTMENT;
            default -> throw new BoardsCustomException(BoardsExceptionCode.NOT_FOUND_BOARD_TYPE);
        };
    }*/
}
