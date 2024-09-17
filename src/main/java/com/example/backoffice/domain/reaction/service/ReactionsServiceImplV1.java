package com.example.backoffice.domain.reaction.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.service.BoardsServiceV1;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.comment.service.CommentsServiceV1;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
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
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReactionsServiceImplV1 implements ReactionsServiceV1 {

    private final ReactionsRepository reactionsRepository;
    private final MembersServiceV1 membersService;
    private final BoardsServiceV1 boardsService;
    private final CommentsServiceV1 commentsService;
    private final NotificationsServiceFacadeV1 notificationsServiceFacade;

    @Override
    @Transactional
    public ReactionsResponseDto.CreateOneForMemberDto createOneForMember(
            Long toMemberId, Members fromMember, ReactionsRequestDto requestDto) {
        // 1. 자기 자신이 아닌 다른 사람에게 리액션을 하는지?
        Members toMember
                = membersService.checkDifferentMember(fromMember.getId(), toMemberId);

        // 2. 이미 준 사람에게 리액션을 중복해서 주는 것 방지
        if (reactionsRepository.existsByMemberAndReactor(toMember, fromMember)) {
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        // 3. Emoji에 없는 리액션을 하는 것을 방지
        Emoji emoji = validateEmoji(requestDto.getEmoji(), Collections.singleton(Emoji.LOVE));

        Reactions reaction
                = ReactionsConverter.toEntity(toMember, fromMember, emoji, null, null, null);

        toMember.addLoveCount();
        reactionsRepository.save(reaction);

        // toMember, fromMember 정보가 notification으로 다 넘어가기에 Member Domain은 null 가능
        NotificationData membersNotification =
                new NotificationData(toMember, fromMember, null, null, null, null, null);
        notificationsServiceFacade.createOne(membersNotification, NotificationType.MEMBER);
        return ReactionsConverter.toCreateMemberReactionDto(reaction, emoji.toString());
    }

    @Override
    @Transactional
    public void deleteOneForMember(
            Long toMemberId, Long reactionId, Members fromMember) {
        Members toMember
                = membersService.checkDifferentMember(fromMember.getId(), toMemberId);
        if (!reactionsRepository.existsByIdAndMemberAndReactor(reactionId, toMember, fromMember)) {
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION);
        }
        toMember.deleteLoveCount();

        reactionsRepository.deleteById(reactionId);
    }

    @Override
    @Transactional
    public List<ReactionsResponseDto.ReadOneForBoardDto> readAllForBoard(Long boardId){
        List<Reactions> reactionList
                = reactionsRepository.findByBoardIdAndCommentIsNull(boardId);

        return ReactionsConverter.toReadAllForBoardDtoList(reactionList);
    }

    @Override
    @Transactional
    public List<ReactionsResponseDto.ReadOneForCommentDto> readAllForComment(Long boardId){
        List<Reactions> reactionList
                = reactionsRepository.findByBoardIdAndCommentIsNotNullAndReplyIsNull(boardId);
        return ReactionsConverter.toReadAllForCommentDtoList(reactionList);
    }

    @Override
    @Transactional
    public List<ReactionsResponseDto.ReadOneForReplyDto> readAllForReply(Long commentId){
        List<Reactions> reactionList
                = reactionsRepository.findByCommentIdAndReplyIsNotNull(commentId);
        return ReactionsConverter.toReadAllForReplyDtoList(reactionList);
    }

    @Override
    @Transactional
    public ReactionsResponseDto.CreateOneForBoardDto createOneForBoard(
            Long boardId, Members fromMember,
            ReactionsRequestDto requestDto) {
        Boards board = boardsService.findById(boardId);
        membersService.checkDifferentMember(
                fromMember.getId(), board.getMember().getId());
        Emoji emoji = validateEmoji(requestDto.getEmoji(), EnumSet.of(Emoji.LIKE, Emoji.UNLIKE));

        if (reactionsRepository.existsByBoardAndReactor(
                board, fromMember)) {
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        Reactions reaction = ReactionsConverter.toEntity(null, fromMember, emoji, board, null, null);
        board.addEmoji(reaction, emoji.toString());
        reactionsRepository.save(reaction);

        NotificationData boardsNotification =
                new NotificationData(board.getMember(), fromMember, board, null, null, null, null);
        notificationsServiceFacade.createOne(boardsNotification, NotificationType.BOARD);
        return ReactionsConverter.toCreateBoardReactionDto(
                reaction.getId(), fromMember, board, emoji.toString());
    }

    @Override
    @Transactional
    public void deleteOneForBoard(Long boardId, Long reactionId, Members member) {

        // 1. 예외 검증
        Boards board = boardsService.findById(boardId);
        membersService.checkDifferentMember(member.getId(), board.getMember().getId());
        if (!reactionsRepository.existsByIdAndBoardAndReactor(reactionId, board, member)) {
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION);
        }

        Reactions reaction = findById(reactionId);

        board.deleteEmoji(reaction.getEmoji().toString());
        reactionsRepository.deleteById(reactionId);
    }

    @Override
    @Transactional
    public ReactionsResponseDto.CreateOneForCommentDto createOneForComment(
            Long boardId, Long commentId, Members fromMember,
            ReactionsRequestDto requestDto) {

        // 1. 예외 처리
        Boards board = boardsService.findById(boardId);
        Comments comment = commentsService.findById(commentId);

        Emoji emoji = validateEmoji(requestDto.getEmoji(), EnumSet.of(Emoji.LIKE, Emoji.UNLIKE));

        if (reactionsRepository.existsByCommentAndReactor(
                comment, fromMember)) {
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        // 2. 엔티티 저장
        Reactions reaction = ReactionsConverter.toEntity(null, fromMember, emoji, board, comment, null);
        comment.addEmoji(reaction, emoji.toString());
        reactionsRepository.save(reaction);

        // 3. 알림 전달
        NotificationData commentsNotification =
                new NotificationData(
                        comment.getMember(), fromMember, board, comment, null, null, null);
        notificationsServiceFacade.createOne(commentsNotification, NotificationType.COMMENT);

        // 4. DTO 변환
        return ReactionsConverter.toCreateCommentReactionDto(
                reaction.getId(), comment, fromMember, emoji.toString());
    }

    @Override
    @Transactional
    public void deleteOneForComment(
            Long commentId, Long reactionId, Members fromMember) {
        Comments comment = commentsService.findById(commentId);
        Reactions reaction = findById(reactionId);

        if (!reactionsRepository.existsByIdAndCommentAndReactor(
                reactionId, comment, fromMember)) {
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION);
        }
        String commentEmoji = reaction.getEmoji().toString();

        comment.deleteEmoji(commentEmoji);

        reactionsRepository.deleteById(reactionId);
    }

    @Override
    @Transactional
    public ReactionsResponseDto.CreateOneForReplyDto createOneForReply(
            Long commentId, Long replyId, Members fromMember,
            ReactionsRequestDto requestDto) {
        Comments comment = commentsService.findById(commentId);
        Comments reply = commentsService.findById(replyId);

        Emoji replyEmoji = validateEmoji(requestDto.getEmoji(), EnumSet.of(Emoji.LIKE, Emoji.UNLIKE));

        if (reactionsRepository.existsByCommentAndReactor(
                reply, fromMember)) {
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        Reactions reaction = ReactionsConverter.toEntity(null, fromMember, replyEmoji, null, comment, reply);
        reactionsRepository.save(reaction);

        reply.addEmoji(reaction, replyEmoji.toString());
        NotificationData replyNotification =
                new NotificationData(
                        reply.getMember(), fromMember,
                        reply.getParent().getBoard(), comment, reply, null, null);
        notificationsServiceFacade.createOne(replyNotification, NotificationType.REPLY);
        return ReactionsConverter.toCreateReplyReactionDto(
                reaction.getId(), reply, fromMember, replyEmoji.toString());
    }

    @Override
    @Transactional
    public void deleteOneForReply(
            Long replyId, Long reactionId, Members fromMember) {
        Comments reply = commentsService.findById(replyId);
        Reactions reaction = findById(reactionId);

        if (!reaction.getReply().getId().equals(replyId) ||
                !reaction.getReactor().getId().equals(fromMember.getId())) {
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

    public Reactions findById(Long reactionId) {
        return reactionsRepository.findById(reactionId).orElseThrow(
                () -> new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION)
        );
    }
}
