package com.example.backoffice.domain.board.controller;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.facade.BoardsServiceFacadeV1;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Boards API", description = "게시글 API")
public class BoardsController {

    private final BoardsServiceFacadeV1 boardsServiceFacade;

    @GetMapping("/boards")
    @Operation(summary = "전체 타입 게시글 페이지 조회",
            description = "로그인 사용자는 전체 타입의 제목, 중요도, " +
                    "요약된 내용 정보를 담은 전체 타입 게시글 페이지 형태로 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 타입 게시글 페이지 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = BoardsResponseDto.ReadAllDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Page<BoardsResponseDto.ReadAllDto>> readPageGeneralType(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(size = 8) Pageable pageable) {
        Page<BoardsResponseDto.ReadAllDto> responseDtoList
                = boardsServiceFacade.readPageGeneralType(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @GetMapping("/boards/{boardId}")
    @Operation(summary = "전체 타입 게시글 한 개 조회.",
            description = "로그인 사용자는 전체 타입의 게시글 하나를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 타입 게시글 하나 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardsResponseDto.ReadOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<BoardsResponseDto.ReadOneDto> readOneGeneralType(
            @PathVariable long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        BoardsResponseDto.ReadOneDto responseDto
                = boardsServiceFacade.readOneGeneralType(boardId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping(
            value = "/boards",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "전체 타입 게시글 한 개 생성",
            description = "부장 직급 이상의 멤버는 전체 타입의 게시글 한 개를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 타입 게시글 한 개 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardsResponseDto.CreateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<BoardsResponseDto.CreateOneDto> createOneGeneralType(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid BoardsRequestDto.CreateOneDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        BoardsResponseDto.CreateOneDto responseDto
                = boardsServiceFacade.createOneGeneralType(
                        memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping(
            value = "/boards/{boardId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "전체 타입 게시글 한 개 수정",
            description = "자신이 만든 전체 타입의 게시글을 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 타입 게시글 한 개 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardsResponseDto.UpdateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<BoardsResponseDto.UpdateOneDto> updateOneGeneralType(
            @PathVariable long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") BoardsRequestDto.UpdateOneDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        BoardsResponseDto.UpdateOneDto responseDto
                = boardsServiceFacade.updateOneGeneralType(boardId, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/boards/{boardId}")
    @Operation(summary = "모든 타입의 게시글 한 개 삭제",
            description = "자신이 만든 전체 타입의 게시글을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 타입의 게시글 한 개 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponse<Void>> deleteOne(
            @PathVariable long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        boardsServiceFacade.deleteOne(boardId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        200,
                        "게시글 하나 삭제 성공",
                        null));
    }

    @GetMapping("/departments/{department}/boards")
    @Operation(summary = "부서 타입 게시글 페이지 조회",
            description = "로그인 사용자는 전체 타입의 제목, 중요도, 잠금 여부, " +
                    "요약된 내용 정보를 담은 부서 타입 게시글 페이지 형태로 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 타입 게시글 페이지 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = BoardsResponseDto.ReadAllDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Page<BoardsResponseDto.ReadAllDto>> readPageDepartmentType(
            @PathVariable(name = "department") String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardsResponseDto.ReadAllDto> responseDtoList
                = boardsServiceFacade.readPageDepartmentType(
                        department, memberDetails.getMembers(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @GetMapping("/departments/{department}/boards/{boardId}")
    @Operation(summary = "부서 타입 게시글 한 개 조회",
            description = "로그인한 사용자는 부서 타입 게시글을 한 개 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 타입 게시글 한 개 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardsResponseDto.ReadOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<BoardsResponseDto.ReadOneDto> readOneDepartmentType(
            @PathVariable(name = "department") String department,
            @PathVariable(name = "boardId") Long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        BoardsResponseDto.ReadOneDto responseDto
                = boardsServiceFacade.readOneDepartmentType(
                        department, boardId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping(
            value = "/departments/{department}/boards",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "부서 타입 게시글 한 개 생성",
            description = "로그인 사용자는 자기 자신의 부서에 해당하는 부서 게시글을 작성 할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 타입 게시글 한 개 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardsResponseDto.CreateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<BoardsResponseDto.CreateOneDto> createOneDepartmentType(
            @PathVariable String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid BoardsRequestDto.CreateOneDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        BoardsResponseDto.CreateOneDto responseDto
                = boardsServiceFacade.createOneDepartmentType(
                        department, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping(
            value = "/departments/{department}/boards/{boardId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "부서 타입 게시글 한 개 수정",
            description = "자신이 작성한 부서 타입의 게시글 한 개를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 타입 게시글 한 개 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardsResponseDto.UpdateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "게시글 한 개의 중요도 수정",
            description = "자신이 작성한 게시글의 중요도를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 한 개의 중요도 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "게시글 한 개의 잠금 여부 수정",
            description = "자신이 작성한 게시글의 잠금 여부를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 한 개의 잠금 여부 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponse<Void>> updateDepartmentForMarkAsLocked(
            @PathVariable Long boardId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        boardsServiceFacade.updateOneForMarkAsLocked(boardId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        200, "부서 게시글 잠금 상태 변경 성공", null));
    }
}
