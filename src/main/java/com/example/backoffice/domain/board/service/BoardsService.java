package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardsService {

    public Page<BoardsResponseDto.ReadBoardListResponseDto> readBoard(Pageable pageable);
    public BoardsResponseDto.ReadBoardResponseDto readOne(Long boardId);
    public BoardsResponseDto.CreateBoardResponseDto createBoard(
            Members member, BoardsRequestDto.CreateBoardRequestDto requestDto,
            List<MultipartFile> files);
    public BoardsResponseDto.UpdateBoardResponseDto updateBoard(
            Long boardId, Members member,
            BoardsRequestDto.UpdateBoardRequestDto requestDto,
            List <MultipartFile> files);
    Boards findById(Long boardId);
}
