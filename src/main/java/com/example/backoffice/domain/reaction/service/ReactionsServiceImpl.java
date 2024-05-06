package com.example.backoffice.domain.reaction.service;

import com.example.backoffice.domain.board.converter.BoardsConverter;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.service.BoardsService;
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

@Service
@RequiredArgsConstructor
public class ReactionsServiceImpl implements ReactionsService{

    private final ReactionsRepository reactionsRepository;
    private final MembersService membersService;
    private final BoardsService boardsService;

    @Override
    @Transactional
    public ReactionsResponseDto.CreateMemberReactionResponseDto createMemberReaction
            (Long toMemberId, Members fromMember,
             ReactionsRequestDto.CreateMemberReactionsRequestDto requestDto){
        Members toMember
                = membersService.isMatchedLoginMember(toMemberId, fromMember.getId());
        if (reactionsRepository.existsByMemberAndReactorAndEmoji(
                toMember, fromMember, Emoji.valueOf(requestDto.getEmoji().toUpperCase()))) {
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        Emoji emoji = isMatchedMemberEmoji(requestDto.getEmoji());

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
        Members toMember = membersService.isMatchedLoginMember(toMemberId, fromMember.getId());
        if (!reactionsRepository.existsByIdAndMemberAndReactor(reactionId, toMember, fromMember)) {
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_FOUND_REACTION);
        }
        Reactions reaction = findById(reactionId);

        toMember.deleteEmoji();

        reactionsRepository.delete(reaction);
    }

    @Override
    @Transactional
    public ReactionsResponseDto.CreateBoardReactionResponseDto createBoardReaction(
            Long boardId, Members fromMember,
            ReactionsRequestDto.CreateBoardReactionRequestDto requestDto){

        // 1. board 존재하는지
        // 2. board 작성자와 이모지 반응을 주는 사람과 일치 하는지?
        Boards board = boardsService.findById(boardId);
        membersService.isMatchedLoginMember(
                board.getMember().getId(), fromMember.getId());
        Emoji emoji = isMatchedEmoji(requestDto.getEmoji());

        if(reactionsRepository.existsByBoardAndReactorAndEmoji(
                board, fromMember, emoji)){
            throw new ReactionsCustomException(ReactionsExceptionCode.EMOJI_ALREADY_EXISTS);
        }

        Reactions reaction = ReactionsConverter.toEntity(null, fromMember, emoji, board, null);
        board.addEmoji(reaction, emoji.toString());

        return ReactionsConverter.toCreateBoardReactionDto(
                fromMember, board, reaction, emoji.toString());
    }

    private Emoji isMatchedMemberEmoji(String memberEmoji){
        Emoji emoji;
        try {
            emoji = Emoji.valueOf(memberEmoji.toUpperCase()); // 클라이언트로부터 받은 문자열을 Emoji enum으로 변환
            if(emoji.toString().equals("LOVE")){
                return emoji;
            }else{
                // 멤버는 "LOVE" 이모지를 제외하고 할 수 없음
                throw new ReactionsCustomException(ReactionsExceptionCode.NOT_ACCEPTED_EMOJI);
            }

        } catch (IllegalArgumentException e) {
            throw new ReactionsCustomException(ReactionsExceptionCode.NOT_MATCHED_EMOJI);
        }
    }

    private Emoji isMatchedEmoji(String requestEmoji){
        Emoji emoji;
        try {
            emoji = Emoji.valueOf(requestEmoji.toUpperCase()); // 클라이언트로부터 받은 문자열을 Emoji enum으로 변환
            if(emoji.toString().equals("LIKE") || emoji.toString().equals("UNLIKE")){
                return emoji;
            }else{
                // 게시글, 댓글, 대댓글은 "LIKE", "UNLIKE"만 사용 가능
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
