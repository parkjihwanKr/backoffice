package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface BoardsService {

    public List<BoardsResponseDto.ReadBoardListResponseDto> readBoard();
    public BoardsResponseDto.ReadBoardResponseDto readPost(Long boardId);
    public BoardsResponseDto.CreateBoardResponseDto createPost(
            Long boardId, Members member, BoardsRequestDto.CreateBoardRequestDto requestDto);
}
