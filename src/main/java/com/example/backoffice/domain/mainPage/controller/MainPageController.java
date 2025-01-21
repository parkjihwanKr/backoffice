package com.example.backoffice.domain.mainPage.controller;

import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;
import com.example.backoffice.domain.mainPage.service.MainPageService;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Main Page API", description = "메인 페이지 API")
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/main-page")
    @Operation(summary = "메인 페이지 조회",
            description = "로그인한 사용자가 요약된 부서 일정, 게시판과 " +
                    "개인 일정, 휴가, 근태 기록 등을 메인 페이지에서 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메인 페이지 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = MainPageResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<MainPageResponseDto> read(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return ResponseEntity.status(HttpStatus.OK).body(
                mainPageService.read(memberDetails.getMembers()));
    }
}
