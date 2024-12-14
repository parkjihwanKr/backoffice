package com.example.backoffice.domain.board.controller;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.facade.BoardsServiceFacadeV1;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardsController {

    private final BoardsServiceFacadeV1 boardsServiceFacade;

    // 게시글 전체 읽기
    @GetMapping("/boards")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<BoardsResponseDto.ReadAllDto>> readAll(
            @PageableDefault(size = 8) Pageable pageable) {
        Page<BoardsResponseDto.ReadAllDto> responseDtoList
                = boardsServiceFacade.readAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 게시글 하나 읽기
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardsResponseDto.ReadOneDto> readOne(
            @PathVariable long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        BoardsResponseDto.ReadOneDto responseDto
                = boardsServiceFacade.readOne(boardId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 게시글 게시
    @PostMapping(
            value = "/boards",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BoardsResponseDto.CreateOneDto> createOne(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid BoardsRequestDto.CreateOneDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        BoardsResponseDto.CreateOneDto responseDto
                = boardsServiceFacade.createOne(memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 게시글 수정
    @PatchMapping(
            value = "/boards/{boardId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BoardsResponseDto.UpdateOneDto> updateOne(
            @PathVariable long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") BoardsRequestDto.UpdateOneDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        BoardsResponseDto.UpdateOneDto responseDto
                = boardsServiceFacade.updateOne(boardId, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<CommonResponse<Void>> deleteOne(
            @PathVariable long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        boardsServiceFacade.deleteOne(boardId, memberDetails.getMembers());
        return ResponseEntity.ok().body(new CommonResponse<>(HttpStatus.OK, "게시글 삭제 성공"));
    }

    // 부서별 전체 게시글 읽기
    @GetMapping("/departments/{department}/boards")
    public ResponseEntity<Page<BoardsResponseDto.ReadAllDto>> readAllForDepartment(
            @PathVariable String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardsResponseDto.ReadAllDto> responseDtoList
                = boardsServiceFacade.readAllForDepartment(
                        department, memberDetails.getMembers(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 부서별 게시글 하나 읽기
    @GetMapping("/departments/{department}/boards/{boardId}")
    public ResponseEntity<BoardsResponseDto.ReadOneDto> readOneForDepartment(
            @PathVariable String department,
            @PathVariable Long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        BoardsResponseDto.ReadOneDto responseDto
                = boardsServiceFacade.readOneForDepartment(
                        department, boardId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 부서별 게시글 생성
    @PostMapping(
            value = "/departments/{department}/boards",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BoardsResponseDto.CreateOneDto> createOneForDepartment(
            @PathVariable String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid BoardsRequestDto.CreateOneDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        BoardsResponseDto.CreateOneDto responseDto
                = boardsServiceFacade.createOneForDepartment(
                        department, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 부서별 게시글 수정
    @PatchMapping(value = "/departments/{department}/boards/{boardId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BoardsResponseDto.UpdateOneDto> updateOneForDepartment(
            @PathVariable String department,
            @PathVariable Long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid BoardsRequestDto.UpdateOneDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        BoardsResponseDto.UpdateOneDto responseDto
                = boardsServiceFacade.updateOneForDepartment(
                        department, boardId, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 게시판의 중요도 수정
    @PatchMapping("/boards/{boardId}/important")
    public ResponseEntity<CommonResponse<Void>> updateOneForMarkAsImportant(
            @PathVariable Long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        boardsServiceFacade.updateOneForMarkAsImportant(boardId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        200, "게시글 중요 체크 성공", null));
    }

    // 부서 게시판의 잠금 수정
    @PatchMapping("/boards/{boardId}/lock")
    public ResponseEntity<CommonResponse<Void>> updateDepartmentForMarkAsLocked(
            @PathVariable Long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        boardsServiceFacade.updateOneForMarkAsLocked(boardId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        200, "부서 게시글 잠금 상태 변경 성공", null));
    }
}
