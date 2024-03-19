package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.dto.BoardsResponseDto;

import java.util.List;

public interface BoardsService {

    public List<BoardsResponseDto.ReadBoardListResponseDto> readBoard();
    public BoardsResponseDto.ReadBoardResponseDto readPost(Long boardId);
}
