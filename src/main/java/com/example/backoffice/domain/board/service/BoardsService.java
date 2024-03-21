package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardsService {

    public Page<BoardsResponseDto.ReadBoardListResponseDto> readBoard(Pageable pageable);
    public BoardsResponseDto.ReadBoardResponseDto readPost(Long boardId);
    public BoardsResponseDto.CreateBoardResponseDto createPost(
            Long boardId, Members member, BoardsRequestDto.CreateBoardRequestDto requestDto);
    public BoardsResponseDto.UpdateBoardResponseDto updatePost(
            Long boardId, Members member, BoardsRequestDto.UpdateBoardRequestDto requestDto);
}
