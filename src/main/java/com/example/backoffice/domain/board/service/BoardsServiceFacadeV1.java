package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardsServiceFacadeV1 {

    Page<BoardsResponseDto.ReadAllDto> readAll(Pageable pageable);

    BoardsResponseDto.ReadOneDto readOne(Long boardId);

    BoardsResponseDto.CreateOneDto createOne(
            Members loginMember, BoardsRequestDto.CreateOneDto requestDto,
            List<MultipartFile> files);

    BoardsResponseDto.UpdateOneDto updateOne(
            Long boardId, Members loginMember,
            BoardsRequestDto.UpdateOneDto requestDto,
            List <MultipartFile> files);

    void deleteOne(Long boardId, Members member);

    BoardsResponseDto.CreateOneDto createOneForDepartment(
            String department, Members loginMember,
            BoardsRequestDto.CreateOneDto requestDto, List<MultipartFile> files);

    Page<BoardsResponseDto.ReadAllDto> readAllForDepartment(
            String departmentName, Pageable pageable);

    BoardsResponseDto.ReadOneDto readOneForDepartment(
            String departmentName, Long boardId);

    BoardsResponseDto.UpdateOneDto updateOneForDepartment(
            String department, Long boardId, Members loginMember,
            BoardsRequestDto.UpdateOneDto requestDto,
            List<MultipartFile> files);

    void updateOneForMarkAsImportant(Long boardId, Members loginMember);

    void updateOneForMarkAsLocked(Long boardId, Members LoginMember);
}
