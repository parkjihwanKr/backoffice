package com.example.backoffice.domain.comment.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.service.BoardsService;
import com.example.backoffice.domain.comment.converter.CommentsConverter;
import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.comment.exception.CommentsCustomException;
import com.example.backoffice.domain.comment.exception.CommentsExceptionCode;
import com.example.backoffice.domain.comment.repository.CommentsRepository;
import com.example.backoffice.domain.member.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService{

    private final CommentsRepository commentsRepository;
    private final BoardsService boardsService;

    @Override
    @Transactional
    public CommentsResponseDto.CreateCommentsResponseDto createComment(
            CommentsRequestDto.CreateCommentsRequestDto requestDto,
            Long boardId, Members member) {
        Boards board = boardsService.findById(boardId);
        Comments comment = CommentsConverter.toEntity(requestDto, board, member);
        board.addComment(comment);
        commentsRepository.save(comment);
        return CommentsConverter.toCreateDto(comment, member);
    }

    @Override
    @Transactional
    public CommentsResponseDto.UpdateCommentsResponseDto updateComment(
            Long boardId, Long commentId,
            CommentsRequestDto.UpdateCommentsRequestDto requestDto,
            Members member){
        Boards board = boardsService.findById(boardId);
        Comments comment = findById(commentId);
        if(!comment.getBoard().getId().equals(board.getId())){
            throw new CommentsCustomException(CommentsExceptionCode.NOT_MATCHED_BOARD_COMMENT);
        }
        if(!comment.getMember().getId().equals(member.getId())){
            throw new CommentsCustomException(CommentsExceptionCode.NOT_MATCHED_MEMBER_COMMENT);
        }
        comment.update(requestDto);
        return CommentsConverter.toUpdateDto(comment, member);
    }

    public Comments findById(Long commentId){
        return commentsRepository.findById(commentId).orElseThrow(
                () -> new CommentsCustomException(CommentsExceptionCode.NOT_FOUND_COMMENT)
        );
    }
}
