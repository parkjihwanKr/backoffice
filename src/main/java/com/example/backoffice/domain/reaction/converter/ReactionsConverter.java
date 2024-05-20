package com.example.backoffice.domain.reaction.converter;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.entity.Emoji;
import com.example.backoffice.domain.reaction.entity.Reactions;

public class ReactionsConverter {

    public static Reactions toEntity(
            Members toMember, Members fromMember,
            Emoji emoji, Boards board, Comments comment) {
        return Reactions.builder()
                .board(board)
                .comment(comment)
                .member(toMember)
                .reactor(fromMember)
                .emoji(emoji)
                .build();
    }

    public static ReactionsResponseDto
            .CreateMemberReactionResponseDto toCreateMemberReactionDto(
                    Reactions reaction, String emoji){
        return ReactionsResponseDto.CreateMemberReactionResponseDto.builder()
                .fromMemberName(reaction.getReactor().getMemberName())
                .toMemberName(reaction.getMember().getMemberName())
                .emoji(emoji)
                .createdAt(reaction.getCreatedAt())
                .build();
    }

    public static ReactionsResponseDto
            .CreateBoardReactionResponseDto toCreateBoardReactionDto(
                    Members fromMember, Boards board, String emoji){
        return ReactionsResponseDto.CreateBoardReactionResponseDto.builder()
                .boardTitle(board.getTitle())
                .boardContent(board.getContent())
                .likeCount(board.getLikeCount())
                .unLikeCount(board.getUnLikeCount())
                .viewCount(board.getViewCount())
                .fromMemberName(fromMember.getMemberName())
                .emoji(emoji)
                .build();
    }

    public static ReactionsResponseDto
            .CreateCommentReactionResponseDto toCreateCommentReactionDto(
                    Comments comment, Members fromMember, String emoji){
        return ReactionsResponseDto.CreateCommentReactionResponseDto.builder()
                .commentContent(comment.getContent())
                .likeCount(comment.getLikeCount())
                .unLikeCount(comment.getUnLikeCount())
                .fromMemberName(fromMember.getMemberName())
                .emoji(emoji)
                .build();
    }

    public static ReactionsResponseDto
            .CreateReplyReactionResponseDto toCreateReplyReactionDto(
                    Comments reply, Members fromMember, String emoji){
        return ReactionsResponseDto.CreateReplyReactionResponseDto.builder()
                .replyContent(reply.getContent())
                .likeCount(reply.getLikeCount())
                .unLikeCount(reply.getUnLikeCount())
                .fromMemberName(fromMember.getMemberName())
                .emoji(emoji)
                .build();
    }
}
