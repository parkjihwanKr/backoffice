package com.example.backoffice.domain.board.controller;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.service.BoardsServiceV1;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardsController {

    private final BoardsServiceV1 boardsService;

    // 게시글 전체 읽기
    @GetMapping
    public ResponseEntity<Page<BoardsResponseDto.ReadAllDto>> readAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardsResponseDto.ReadAllDto> responseDtoList =
                boardsService.readAll(pageable);
        return ResponseEntity.ok(responseDtoList);
    }

    // 게시글 하나 읽기
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardsResponseDto.ReadOneDto> readOne(@PathVariable long boardId){
        BoardsResponseDto.ReadOneDto responseDto = boardsService.readOne(boardId);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 게시
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BoardsResponseDto.CreateOneDto> createOne(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid BoardsRequestDto.CreateOneDto requestDto,
            @RequestPart(value = "files") List<MultipartFile> files){
        BoardsResponseDto.CreateOneDto responseDto =
                boardsService.createOne(memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 수정
    @PatchMapping(value = "/{boardId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BoardsResponseDto.UpdateOneDto> updateOne(
            @PathVariable long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") BoardsRequestDto.UpdateOneDto requestDto,
            @RequestPart(value = "files") List<MultipartFile> files){
        BoardsResponseDto.UpdateOneDto responseDto
                = boardsService.updateOne(
                        boardId, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResponse<Void>> deleteOne(
            @PathVariable long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        boardsService.deleteOne(boardId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        HttpStatus.OK,
                        "게시글 삭제 성공"
                )
        );
    }

    // 부서별 게시글 읽기

    // 부서별 게시글 생성 - {MemberDepartment : IT, SALES, MARKETING, FINANCE, ...}
    @PostMapping(
            value = "/department",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BoardsResponseDto.CreateOneForDepartmentDto> createOneForDepartment(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid BoardsRequestDto.CreateOneForDepartmentDto requestDto,
            @RequestPart(value = "files") List<MultipartFile> files){
        BoardsResponseDto.CreateOneForDepartmentDto responseDto
                = boardsService.createOneForDepartment(
                        memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    // 부서별 게시글 수정
    // 부서별 게시글 삭제
}
