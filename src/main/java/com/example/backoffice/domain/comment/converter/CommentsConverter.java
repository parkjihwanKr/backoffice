package com.example.backoffice.domain.comment.converter;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;

public class CommentsConverter {

    public static Comments toEntity(
            CommentsRequestDto.CreateCommentDto requestDto,
            Boards board, Members member){
        return Comments.builder()
                .member(member)
                .board(board)
                .likeCount(0L)
                .unLikeCount(0L)
                .content(requestDto.getContent())
                .build();
    }

    public static CommentsResponseDto.CreateCommentDto toCreateCommentDto(
            Comments comment, Members member){
        return CommentsResponseDto.CreateCommentDto.builder()
                .writer(member.getMemberName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentsResponseDto.UpdateCommentDto toUpdateCommentDto(
            Comments comment, Members member){
        return CommentsResponseDto.UpdateCommentDto.builder()
                .writer(member.getMemberName())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .unLikeCount(comment.getUnLikeCount())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }

    public static Comments toReplyEntity(
            CommentsRequestDto.CreateReplyDto requestDto,
            Boards board, Members member){
        return Comments.builder()
                .member(member)
                .board(board)
                .likeCount(0L)
                .unLikeCount(0L)
                .content(requestDto.getContent())
                .build();
    }

    public static CommentsResponseDto.CreateReplyDto toCreateReplyDto(
            Comments parentComment, Comments childComment, Members member){
        return CommentsResponseDto.CreateReplyDto.builder()
                .toMemberName(parentComment.getMember().getMemberName())
                .parentContent(parentComment.getContent())
                .parentCreatedAt(parentComment.getCreatedAt())
                .fromMemberName(member.getMemberName())
                .childContent(childComment.getContent())
                .childCreatedAt(parentComment.getCreatedAt())
                .build();
    }

    public static CommentsResponseDto.UpdateReplyDto UpdateReplyDto(
            Comments parentComment, Comments childComment, Members member){
        return CommentsResponseDto.UpdateReplyDto.builder()
                .toMemberName(parentComment.getMember().getMemberName())
                .parentContent(parentComment.getContent())
                .parentCreatedAt(parentComment.getCreatedAt())
                .parentModifiedAt(parentComment.getModifiedAt())
                .parentLikeCount(parentComment.getLikeCount())
                .fromMemberName(member.getMemberName())
                .childContent(childComment.getContent())
                .childCreatedAt(childComment.getCreatedAt())
                .childModifiedAt(childComment.getModifiedAt())
                .childLikeCount(childComment.getLikeCount())
                .childUnLikeCount(childComment.getUnLikeCount())
                .build();
    }
}
