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
        comment.updateParent(comment);

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

        isMatchedBoard(comment, board);
        isMatchedMember(comment, member);

        comment.update(requestDto.getContent());

        return CommentsConverter.toUpdateDto(comment, member);
    }

    @Override
    @Transactional
    public void deleteComment(Long boardId, Long commentId, Members member){
        Boards board = boardsService.findById(boardId);
        Comments comment = findById(commentId);

        isMatchedBoard(comment, board);
        isMatchedMember(comment, member);

        commentsRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentsResponseDto.CreateReplyResponseDto createReply(
            Long boardId, Long commentId,
            CommentsRequestDto.CreateReplyRequestDto requestDto,
            Members member){
        Boards board = boardsService.findById(boardId);
        Comments comment = findById(commentId);

        Comments reply = CommentsConverter.toChildEntity(requestDto, board, member);

        isMatchedBoard(comment, board);
        reply.updateParent(comment);
        comment.addReply(reply);
        board.addReply(comment);

        commentsRepository.save(reply);
        return CommentsConverter.toCreateReplyDto(comment, reply, member);
    }

    @Override
    @Transactional
    public CommentsResponseDto.UpdateReplyResponseDto updateReply(
            Long commentId, Long replyId,
            CommentsRequestDto.UpdateReplyRequestDto requestDto,
            Members member){
        Comments comment = findById(commentId);
        Comments reply = findById(replyId);
        Boards board = boardsService.findById(comment.getBoard().getId());

        isMatchedBoard(comment, board);
        isMatchedComment(comment, reply);

        reply.update(requestDto.getContent());

        return CommentsConverter.UpdateReplyDto(comment, reply, member);
    }

    @Override
    @Transactional
    public void deleteReply(Long commentId, Long replyId, Members member){
        Comments comment = findById(commentId);
        Comments reply = findById(replyId);
        Boards board = boardsService.findById(comment.getBoard().getId());

        isMatchedComment(comment, reply);
        isMatchedBoard(comment, board);

        // 대댓글 삭제 로직
        comment.getReplies().removeIf(
                commentReply -> commentReply.getId().equals(replyId));

        commentsRepository.deleteById(replyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Comments findById(Long commentId){
        return commentsRepository.findById(commentId).orElseThrow(
                () -> new CommentsCustomException(CommentsExceptionCode.NOT_FOUND_COMMENT)
        );
    }

    private void isMatchedBoard(Comments comment, Boards board){
        if(!comment.getBoard().getId().equals(board.getId())){
            throw new CommentsCustomException(
                    CommentsExceptionCode.NOT_MATCHED_BOARD_COMMENT
            );
        }
    }

    private void isMatchedMember(Comments comment, Members member){
        if(!comment.getMember().getId().equals(member.getId())){
            throw new CommentsCustomException(
                    CommentsExceptionCode.NOT_MATCHED_MEMBER_COMMENT
            );
        }
    }

    private void isMatchedComment(Comments comment, Comments reply){
        if(comment.getId().equals(reply.getId())){
            throw new CommentsCustomException(CommentsExceptionCode.IS_COMMENT);
        }
    }
}
