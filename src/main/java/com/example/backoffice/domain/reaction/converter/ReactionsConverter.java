package com.example.backoffice.domain.reaction.converter;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.entity.Emoji;
import com.example.backoffice.domain.reaction.entity.Reactions;

import java.util.ArrayList;
import java.util.List;

public class ReactionsConverter {

    public static Reactions toEntity(
            Members toMember, Members fromMember,
            Emoji emoji, Boards board, Comments comment, Comments reply) {
        return Reactions.builder()
                .board(board)
                .comment(comment)
                .reply(reply)
                .member(toMember)
                .reactor(fromMember)
                .emoji(emoji)
                .build();
    }

    public static ReactionsResponseDto.CreateOneForMemberDto toCreateMemberReactionDto(
            Reactions reaction, String emoji) {
        return ReactionsResponseDto.CreateOneForMemberDto.builder()
                .reactionId(reaction.getId())
                .reactorName(reaction.getReactor().getName())
                .toMemberName(reaction.getMember().getName())
                .emoji(emoji)
                .createdAt(reaction.getCreatedAt())
                .build();
    }

    public static ReactionsResponseDto.CreateOneForBoardDto toCreateBoardReactionDto(
            Long reactionId, Members fromMember, Boards board, String emoji) {
        return ReactionsResponseDto.CreateOneForBoardDto.builder()
                .reactionId(reactionId)
                .boardTitle(board.getTitle())
                .boardContent(board.getContent())
                .likeCount(board.getLikeCount())
                .reactorName(fromMember.getName())
                .emoji(emoji)
                .build();
    }

    public static List<ReactionsResponseDto.ReadOneForBoardDto> toReadAllForBoardDtoList(
            List<Reactions> reactionList) {
        List<ReactionsResponseDto.ReadOneForBoardDto> responseDtoList = new ArrayList<>();

        for(Reactions reaction : reactionList){
            responseDtoList.add(
                    ReactionsResponseDto.ReadOneForBoardDto.builder()
                            .reactionId(reaction.getId())
                            .reactorId(reaction.getReactor().getId())
                            .reactorName(reaction.getReactor().getName())
                            .emoji(reaction.getEmoji())
                            .build());
        }
        return responseDtoList;
    }

    public static List<ReactionsResponseDto.ReadOneForCommentDto> toReadAllForCommentDtoList(
            List<Reactions> reactionList){
        List<ReactionsResponseDto.ReadOneForCommentDto> responseDtoList = new ArrayList<>();

        for(Reactions reaction : reactionList){
            responseDtoList.add(
                    ReactionsResponseDto.ReadOneForCommentDto.builder()
                            .reactionId(reaction.getId())
                            .reactorId(reaction.getReactor().getId())
                            .commentId(reaction.getComment().getId())
                            .reactorName(reaction.getReactor().getName())
                            .emoji(reaction.getEmoji())
                            .build());
        }
        return responseDtoList;
    }

    public static List<ReactionsResponseDto.ReadOneForReplyDto> toReadAllForReplyDtoList(
            List<Reactions> reactionList){
        List<ReactionsResponseDto.ReadOneForReplyDto> responseDtoList = new ArrayList<>();
        for(Reactions reaction : reactionList){
            responseDtoList.add(
                    ReactionsResponseDto.ReadOneForReplyDto.builder()
                            .reactionId(reaction.getId())
                            .reactorId(reaction.getReactor().getId())
                            .commentId(reaction.getComment().getId())
                            .replyId(reaction.getReply().getId())
                            .reactorName(reaction.getReactor().getName())
                            .emoji(reaction.getEmoji())
                            .build());
        }
        return responseDtoList;
    }

    public static ReactionsResponseDto.CreateOneForCommentDto toCreateCommentReactionDto(
            Long reactionId, Comments comment, Members fromMember, String emoji) {
        return ReactionsResponseDto.CreateOneForCommentDto.builder()
                .reactionId(reactionId)
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .reactorName(fromMember.getName())
                .emoji(emoji)
                .build();
    }

    public static ReactionsResponseDto.CreateOneForReplyDto toCreateReplyReactionDto(
            Long reactionId, Comments reply, Members fromMember, String emoji) {
        return ReactionsResponseDto.CreateOneForReplyDto.builder()
                .reactionId(reactionId)
                .content(reply.getContent())
                .likeCount(reply.getLikeCount())
                .reactorName(fromMember.getName())
                .emoji(emoji)
                .build();
    }
}
