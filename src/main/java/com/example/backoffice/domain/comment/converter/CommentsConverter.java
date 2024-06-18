package com.example.backoffice.domain.comment.converter;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.dto.RepliesRequestDto;
import com.example.backoffice.domain.comment.dto.RepliesResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;

public class CommentsConverter {

    public static Comments toEntity(
            CommentsRequestDto.CreateOneDto requestDto,
            Boards board, Members member){
        return Comments.builder()
                .member(member)
                .board(board)
                .likeCount(0L)
                .unLikeCount(0L)
                .content(requestDto.getContent())
                .build();
    }

    public static CommentsResponseDto.CreateOneDto toCreateOneCommentDto(
            Comments comment, Members member){
        return CommentsResponseDto.CreateOneDto.builder()
                .writer(member.getMemberName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentsResponseDto.UpdateOneDto toUpdateOneCommentDto(
            Comments comment, Members member){
        return CommentsResponseDto.UpdateOneDto.builder()
                .writer(member.getMemberName())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .unLikeCount(comment.getUnLikeCount())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }

    public static Comments toReplyEntity(
            RepliesRequestDto.CreateOneDto requestDto,
            Boards board, Members member){
        return Comments.builder()
                .member(member)
                .board(board)
                .likeCount(0L)
                .unLikeCount(0L)
                .content(requestDto.getContent())
                .build();
    }

    public static RepliesResponseDto.CreateOneDto toCreateOneReplyDto(
            Comments parentComment, Comments childComment, Members member){
        return RepliesResponseDto.CreateOneDto.builder()
                .toMemberName(parentComment.getMember().getMemberName())
                .parentContent(parentComment.getContent())
                .parentCreatedAt(parentComment.getCreatedAt())
                .fromMemberName(member.getMemberName())
                .childContent(childComment.getContent())
                .childCreatedAt(parentComment.getCreatedAt())
                .build();
    }

    public static RepliesResponseDto.UpdateOneDto UpdateOneReplyDto(
            Comments parentComment, Comments childComment, Members member){
        return RepliesResponseDto.UpdateOneDto.builder()
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
