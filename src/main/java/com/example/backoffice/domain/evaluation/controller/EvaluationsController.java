package com.example.backoffice.domain.evaluation.controller;

import com.example.backoffice.domain.evaluation.dto.EvaluationsRequestDto;
import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.facade.EvaluationsServiceFacadeV1;
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
@Tag(name = "Evaluations API", description = "설문조사 API")
public class EvaluationsController {

    private final EvaluationsServiceFacadeV1 evaluationsServiceFacade;

    @PostMapping("/evaluations")
    @Operation(summary = "설문조사 한 개 생성",
            description = "특정 권한이 있는 관리자가 원하는 타입의 설문조사를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 설문조사 한 개 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EvaluationsResponseDto.CreateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.CreateOneDto> createOne(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.CreateOneDto requestDto){
        EvaluationsResponseDto.CreateOneDto responseDto
                = evaluationsServiceFacade.createOne(
                        memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/evaluations/{evaluationId}")
    @Operation(summary = "설문조사 한 개 조회",
            description = "로그인 사용자는 년, 분기, 설문조사 타입을 필터링한 설문조사를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설문조사 한 개 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = 
                                    EvaluationsResponseDto.ReadOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.ReadOneDto> readOne(
            @RequestParam(name = "year")Integer year,
            @RequestParam(name = "quarter")Integer quarter,
            @RequestParam(name = "evaluationType", required = false)String evaluationType,
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        EvaluationsResponseDto.ReadOneDto responseDto
                = evaluationsServiceFacade.readOne(
                year, quarter, evaluationType, evaluationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/evaluations-department/{evaluationId}")
    @Operation(summary = "설문조사 한 개 수정",
            description = "관리자에 의해 특정 타입의 설문조사를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설문조사 한 개 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = 
                                    EvaluationsResponseDto.UpdateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.UpdateOneDto> updateOne(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.UpdateOneDto requestDto){
        EvaluationsResponseDto.UpdateOneDto responseDto
                = evaluationsServiceFacade.updateOne(
                        evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/evaluations/{evaluationId}")
    @Operation(summary = "설문조사 한 개 삭제",
            description = "툭정 권한을 가진 사용자가 만든 설문조사를 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회사 설문조사 한 개 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponse<Void>> deleteOne(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        evaluationsServiceFacade.deleteOne(evaluationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        HttpStatus.OK, "해당 설문 조사는 삭제되었습니다.", null));
    }

    @PostMapping("/evaluations/{evaluationId}/submit")
    @Operation(summary = "설문조사 한 개 제출",
            description = "로그인한 사용자가 설문조사를 제출할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설문조사 한 개 제출 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.SubmitOneDto> submitOne(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.SubmitOneDto requestDto){
        EvaluationsResponseDto.SubmitOneDto responseDto
                = evaluationsServiceFacade.submitOne(
                        evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/evaluations/{evaluationId}/submit")
    @Operation(summary = "설문조사 한 개 제출 취소",
            description = "로그인한 사용자가 설문조사를 제출을 취소 할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설문조사 한 개 제출 취소 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponse<Void>> deleteSubmittedOneForCancellation(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        evaluationsServiceFacade.deleteSubmittedOneForCancellation(
                evaluationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        HttpStatus.OK, "해당 설문조사가 취소 되었습니다.", null));
    }
}
