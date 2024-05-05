package com.example.backoffice.domain.comment.converter;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;

public class CommentsConverter {

    public static Comments toEntity(
            CommentsRequestDto.CreateCommentsRequestDto requestDto,
            Boards board, Members member){
        return Comments.builder()
                .member(member)
                .board(board)
                .content(requestDto.getContent())
                .build();
    }

    public static CommentsResponseDto.CreateCommentsResponseDto toCreateDto(
            Comments comment, Members member){
        return CommentsResponseDto.CreateCommentsResponseDto.builder()
                .writer(member.getMemberName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentsResponseDto.UpdateCommentsResponseDto toUpdateDto(
            Comments comment, Members member){
        return CommentsResponseDto.UpdateCommentsResponseDto.builder()
                .writer(member.getMemberName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }

    public static Comments toChildEntity(
            CommentsRequestDto.CreateReplyRequestDto requestDto,
            Boards board, Members member){
        return Comments.builder()
                .member(member)
                .board(board)
                .content(requestDto.getContent())
                .build();
    }

    public static CommentsResponseDto.CreateReplyResponseDto toCreateReplyDto(
            Comments parentComment, Comments childComment, Members member){
        return CommentsResponseDto.CreateReplyResponseDto.builder()
                .toMemberName(member.getMemberName())
                .parentContent(parentComment.getContent())
                .parentCreatedAt(parentComment.getCreatedAt())
                .parentModifiedAt(childComment.getModifiedAt())
                .fromMemberName(member.getMemberName())
                .childContent(childComment.getContent())
                .childCreatedAt(parentComment.getCreatedAt())
                .childModifiedAt(childComment.getModifiedAt())
                .build();
    }
}
