package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.board.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService{

    private final BoardsRepository boardsRepository;

    @Override
    public List<BoardsResponseDto.ReadBoardListResponseDto> readBoard(){
        List<Boards> boardList = boardsRepository.findAll();
        return BoardsResponseDto.ReadBoardListResponseDto.of(boardList);
    }
    @Override
    public BoardsResponseDto.ReadBoardResponseDto readPost(Long boardId){
        Boards board = findById(boardId);
        return BoardsResponseDto.ReadBoardResponseDto.from(board);
    }

    public Boards findById(Long boardId){
        return boardsRepository.findById(boardId).orElseThrow(
                ()-> new BoardsCustomException(BoardsExceptionCode.NOT_FOUND_BOARD)
        );
    }
}
