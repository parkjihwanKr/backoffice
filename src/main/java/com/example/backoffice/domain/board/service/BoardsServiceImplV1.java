package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.board.repository.BoardsRepository;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsServiceImplV1 implements BoardsServiceV1 {

    private final BoardsRepository boardsRepository;

    @Override
    @Transactional
    public List<Boards> findByIsImportantTrueAndBoardTypeOrderByModifiedAtDesc(BoardType boardType){
        return boardsRepository.findByIsImportantTrueAndBoardTypeOrderByModifiedAtDesc(boardType);
    }

    @Override
    @Transactional
    public Page<Boards> findByIsImportantFalseAndBoardTypeOrderByCreatedAtDesc(
            Pageable pageable, BoardType boardType){
        return boardsRepository.findByIsImportantFalseAndBoardTypeOrderByCreatedAtDesc(pageable, boardType);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCommentListSize(Boards board) {
        return (long) board.getCommentList().size();
    }

    @Override
    @Transactional
    public Page<Boards> findAllByDepartmentAndBoardType(
            Pageable pageable, MemberDepartment department, BoardType boardType){
        return boardsRepository.findAllByDepartmentAndBoardType(pageable, department, boardType);
    }

    @Override
    @Transactional
    public Boards save(Boards board){
        return boardsRepository.save(board);
    }

    @Override
    @Transactional(readOnly = true)
    public Boards findByIdAndDepartment(Long boardId, MemberDepartment department){
        return boardsRepository.findByIdAndDepartment(boardId, department).orElseThrow(
                ()-> new BoardsCustomException(BoardsExceptionCode.NOT_FOUND_BOARD));
    }

    @Override
    @Transactional(readOnly = true)
    public Boards findById(Long boardId) {
        return boardsRepository.findById(boardId).orElseThrow(
                () -> new BoardsCustomException(BoardsExceptionCode.NOT_FOUND_BOARD));
    }

    @Override
    @Transactional
    public void deleteById(Long boardId){
        boardsRepository.deleteById(boardId);
    }
}
