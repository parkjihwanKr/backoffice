package com.example.backoffice.domain.board.controller;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.service.BoardsService;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardsController {

    private final BoardsService boardsService;

    // 게시글 전체 읽기
    @GetMapping("/boards")
    public ResponseEntity<Page<BoardsResponseDto.ReadBoardListResponseDto>> readBoard(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardsResponseDto.ReadBoardListResponseDto> responseDtoList =
                boardsService.readBoard(pageable);
        return ResponseEntity.ok(responseDtoList);
    }

    // 게시글 하나 읽기
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardsResponseDto.ReadBoardResponseDto> readPost(@PathVariable long boardId){
        BoardsResponseDto.ReadBoardResponseDto responseDto = boardsService.readPost(boardId);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 게시
    @PostMapping(
            value = "/boards/{boardId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE
            })
    public ResponseEntity<BoardsResponseDto.CreateBoardResponseDto> createPost(
            @PathVariable long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid BoardsRequestDto.CreateBoardRequestDto requestDto,
            @RequestPart(value = "files") List<MultipartFile> files){
        BoardsResponseDto.CreateBoardResponseDto responseDto =
                boardsService.createPost(
                        boardId, memberDetails.getMembers(),
                        requestDto, files);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 수정
    @PatchMapping("/boards/{boardId}")
    public ResponseEntity<BoardsResponseDto.UpdateBoardResponseDto> updatePost(
            @PathVariable long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody BoardsRequestDto.UpdateBoardRequestDto requestDto){
        BoardsResponseDto.UpdateBoardResponseDto responseDto
                = boardsService.updatePost(boardId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 이미지 수정
    @PatchMapping("/boards/{boardId}/boardImage")
    public ResponseEntity<BoardsResponseDto.UpdateImageBoardResponseDto> updatePostImage(
            @PathVariable long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody BoardsRequestDto.UpdateImageBoardRequestDto requestDto){
        BoardsResponseDto.UpdateImageBoardResponseDto responseDto =
                boardsService.updatePostImage(boardId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<BoardsResponseDto> deletePost(
            @PathVariable long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody BoardsRequestDto requestDto){

        return null;
    }
}
