package com.example.backoffice.domain.question.controller;

import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.service.QuestionsServiceV1;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Questions API", description = "설문조사 질문 API")
public class QuestionsController {

    private final QuestionsServiceV1 questionsService;

    @PostMapping("/evaluations/{evaluationId}/questions")
    @Operation(summary = "질문 생성",
            description = "자기 자신이 만든 설문조사의 질문들을 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "질문 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionsResponseDto.CreateAllDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<QuestionsResponseDto.CreateAllDto> createAll(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody QuestionsRequestDto.CreateAllDto requestDto){
        QuestionsResponseDto.CreateAllDto responseDto =
                questionsService.createAll(evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/evaluations/{evaluationId}/questions/{questionId}")
    @Operation(summary = "질문 하나 수정",
            description = "자기 자신이 만든 설문조사의 질문 하나를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "질문 하나 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionsResponseDto.UpdateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<QuestionsResponseDto.UpdateOneDto> updateOne(
            @PathVariable Long evaluationId, @PathVariable Long questionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody QuestionsRequestDto.UpdateOneDto requestDto){
        QuestionsResponseDto.UpdateOneDto responseDto
                = questionsService.updateOne(
                        evaluationId, questionId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/evaluations/{evaluationId}/questions/{questionId}/change-order")
    @Operation(summary = "질문 순서를 수정",
            description = "자기 자신이 만든 설문조사의 질문 순서를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "질문 순서를 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionsResponseDto.UpdateOneForOrderDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<QuestionsResponseDto.UpdateOneForOrderDto> updateOneForChangedOrder(
            @PathVariable Long evaluationId, @PathVariable Long questionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody QuestionsRequestDto.UpdateOneForOrderDto requestDto){
        QuestionsResponseDto.UpdateOneForOrderDto responseDto
                =  questionsService.updateOneForChangedOrder(
                        evaluationId, questionId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/evaluations/{evaluationId}/questions")
    @Operation(summary = "질문 삭제",
            description = "자기 자신이 만든 설문조사의 질문들을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "질문 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponse<Void>> delete(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody QuestionsRequestDto.DeleteDto requestDto){
        questionsService.delete(
                evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        HttpStatus.OK, "성공적으로 삭제되었습니다.", null));
    }
}
