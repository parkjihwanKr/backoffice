package com.example.backoffice.domain.reaction.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.service.BoardsService;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.comment.service.CommentsService;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.reaction.converter.ReactionsConverter;
import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.entity.Emoji;
import com.example.backoffice.domain.reaction.entity.Reactions;
import com.example.backoffice.domain.reaction.exception.ReactionsCustomException;
import com.example.backoffice.domain.reaction.exception.ReactionsExceptionCode;
import com.example.backoffice.domain.reaction.repository.ReactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReactionsServiceImpl implements ReactionsService{

    private final ReactionsRepository reactionsRepository;
    private final MembersService membersService;
    private final BoardsService boardsService;
    private final CommentsService commentsService;

    @Override
    @Transactional
    public ReactionsResponseDto.CreateMemberReactionResponseDto createMemberReaction
            (Long toMemberId, Members fromMember,
             ReactionsRequestDto requestDto){
        Members toMember
                = membersService.validateMember(toMemberId, fromMember.getId());

        if (reactionsRepository.existsByMemberAndReactorAndEmoji(
                toMember, fromMember, Emoji.valueOf(requestDto.getEmoji().toUpperCase()))) {
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        Emoji emoji = validateEmoji(requestDto.getEmoji(), Collections.singleton(Emoji.LOVE));

        Reactions reaction
                = ReactionsConverter.toEntity(toMember, fromMember, emoji, null, null);
        reactionsRepository.save(reaction);

        toMember.addEmoji(reaction);

        return ReactionsConverter.toCreateMemberReactionDto(reaction, emoji.toString());
    }

    @Override
    @Transactional
    public void deleteMemberReaction(
            Long toMemberId, Long reactionId, Members fromMember){
        Members toMember = membersService.validateMember(toMemberId, fromMember.getId());
        if (!reactionsRepository.existsByIdAndMemberAndReactor(reactionId, toMember, fromMember)) {
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION);
        }
        toMember.deleteEmoji();

        reactionsRepository.deleteById(reactionId);
    }

    @Override
    @Transactional
    public ReactionsResponseDto.CreateBoardReactionResponseDto createBoardReaction(
            Long boardId, Members fromMember,
            ReactionsRequestDto requestDto){
        Boards board = boardsService.findById(boardId);
        membersService.validateMember(
                board.getMember().getId(), fromMember.getId());
        Emoji emoji = validateEmoji(requestDto.getEmoji(), EnumSet.of(Emoji.LIKE, Emoji.UNLIKE));

        if(reactionsRepository.existsByBoardAndReactorAndEmoji(
                board, fromMember, emoji)){
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        Reactions reaction = ReactionsConverter.toEntity(null, fromMember, emoji, board, null);
        board.addEmoji(reaction, emoji.toString());

        return ReactionsConverter.toCreateBoardReactionDto(
                fromMember, board, reaction, emoji.toString());
    }

    @Override
    @Transactional
    public void deleteBoardReaction(Long boardId, Long reactionId, Members member){

        // 1. 예외 검증
        Boards board = boardsService.findById(boardId);
        membersService.validateMember(board.getMember().getId(), member.getId());
        if(!reactionsRepository.existsByIdAndBoardAndReactor(reactionId, board, member)){
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION);
        }

        Reactions reaction = findById(reactionId);

        board.deleteEmoji(reaction.getEmoji().toString());
        reactionsRepository.deleteById(reactionId);
        // membersService.isNotMatchedLoginMember(reaction.getReactor().getId(), member.getId());
    }

    @Override
    @Transactional
    public ReactionsResponseDto.CreateCommentReactionResponseDto createCommentReaction(
            Long boardId, Long commentId, Members fromMember,
            ReactionsRequestDto requestDto){

        // 1. 예외 처리
        Boards board = boardsService.findById(boardId);
        Comments comment = commentsService.findById(commentId);

        Emoji emoji = validateEmoji(requestDto.getEmoji(), EnumSet.of(Emoji.LIKE, Emoji.UNLIKE));

        if(reactionsRepository.existsByCommentAndReactorAndEmoji(
                comment, fromMember, emoji)){
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        Reactions reaction = ReactionsConverter.toEntity(null, fromMember, emoji, board, comment);
        comment.addEmoji(reaction, emoji.toString());
        return ReactionsConverter.toCreateCommentReactionDto(comment, fromMember, emoji.toString());
    }

    @Override
    @Transactional
    public void deleteCommentReaction(
            Long commentId, Long reactionId, Members fromMember){
        Comments comment = commentsService.findById(commentId);
        Reactions reaction = findById(reactionId);

        if(!reactionsRepository.existsByIdAndCommentAndReactor(
                reactionId, comment, fromMember)){
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION);
        }
        String commentEmoji = reaction.getEmoji().toString();
        comment.deleteEmoji(commentEmoji);

        reactionsRepository.deleteById(reactionId);
    }

    @Override
    @Transactional
    public ReactionsResponseDto.CreateReplyReactionResponseDto createReplyReaction(
            Long commentId, Long replyId, Members fromMember,
            ReactionsRequestDto requestDto){
        commentsService.findById(commentId);
        Comments reply = commentsService.findById(replyId);

        Emoji replyEmoji = validateEmoji(requestDto.getEmoji(), EnumSet.of(Emoji.LIKE, Emoji.UNLIKE));

        if(reactionsRepository.existsByCommentAndReactorAndEmoji(
                reply, fromMember, replyEmoji)){
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        Reactions reaction = ReactionsConverter.toEntity(null, fromMember, replyEmoji, null, reply);
        reactionsRepository.save(reaction);

        reply.addEmoji(reaction, reaction.toString());
        return ReactionsConverter.toCreateReplyReactionDto(reply, fromMember, replyEmoji.toString());
    }

    @Override
    @Transactional
    public void deleteReplyReaction(
            Long replyId, Long reactionId, Members fromMember){
        Comments reply = commentsService.findById(replyId);
        Reactions reaction = findById(reactionId);

        if(!reactionsRepository.existsByIdAndCommentAndReactor(
                reactionId, reply, fromMember)){
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION);
        }

        String replyEmoji = reaction.getEmoji().toString();
        reply.deleteEmoji(replyEmoji);
        reactionsRepository.deleteById(reactionId);
    }

    private Emoji validateEmoji(String emojiStr, Set<Emoji> validEmojis) {
        try {
            Emoji emoji = Emoji.valueOf(emojiStr.toUpperCase());
            if (validEmojis.contains(emoji)) {
                return emoji;
            } else {
                throw new ReactionsCustomException(ReactionsExceptionCode.NOT_ACCEPTED_EMOJI);
            }
        } catch (IllegalArgumentException e) {
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_MATCHED_EMOJI);
        }
    }


    public Reactions findById(Long reactionId){
        return reactionsRepository.findById(reactionId).orElseThrow(
                ()-> new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION)
        );
    }
}
