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

    @PostMapping("/evaluations-department")
    @Operation(summary = "부서 설문조사 한 개 생성",
            description = "로그인한 사용자의 부서와 일치하는 부서 설문조사를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 설문조사 한 개 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EvaluationsResponseDto.CreateOneForDepartmentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.CreateOneForDepartmentDto> createOneDepartmentType(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.CreateOneForDepartmentDto requestDto){
        EvaluationsResponseDto.CreateOneForDepartmentDto responseDto
                = evaluationsServiceFacade.createOneDepartmentType(
                        memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/evaluations-department/{evaluationId}")
    @Operation(summary = "부서 설문조사 한 개 조회",
            description = "로그인한 사용자의 부서와 일치하고 년, 분기를 필터링한 부서 설문조사를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 설문조사 한 개 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = 
                                    EvaluationsResponseDto.ReadOneForDepartmentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.ReadOneForDepartmentDto> readOneDepartmentType(
            @RequestParam(name = "year")Integer year,
            @RequestParam(name = "quarter")Integer quarter,
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        EvaluationsResponseDto.ReadOneForDepartmentDto responseDto
                = evaluationsServiceFacade.readOneDepartmentType(
                year, quarter, evaluationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/evaluations-department/{evaluationId}")
    @Operation(summary = "부서 설문조사 한 개 수정",
            description = "로그인한 사용자가 만든 부서 설문조사를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 설문조사 한 개 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = 
                                    EvaluationsResponseDto.UpdateOneForDepartmentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.UpdateOneForDepartmentDto> updateOneDepartmentType(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.UpdateOneForDepartmentDto requestDto){
        EvaluationsResponseDto.UpdateOneForDepartmentDto responseDto
                = evaluationsServiceFacade.updateOneDepartmentType(
                evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    
    @PostMapping("/evaluations-company")
    @Operation(summary = "회사 설문조사 한 개 생성",
            description = "인사 부장, 사장만 회사 설문조사를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 설문조사 한 개 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EvaluationsResponseDto.CreateOneForCompanyDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.CreateOneForCompanyDto> createOneCompanyType(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.CreateOneForCompanyDto requestDto){
        EvaluationsResponseDto.CreateOneForCompanyDto responseDto
                = evaluationsServiceFacade.createOneCompanyType(
                        memberDetails.getMembers(),requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/evaluations-company/{evaluationId}")
    @Operation(summary = "회사 설문조사 한 개 조회",
            description = "로그인한 사용자는 년도를 필터링한 회사 설문조사를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회사 설문조사 한 개 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    EvaluationsResponseDto.ReadOneForCompanyDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.ReadOneForCompanyDto> readOneCompanyType(
            @RequestParam(name = "year") Integer year, @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        EvaluationsResponseDto.ReadOneForCompanyDto responseDto
                = evaluationsServiceFacade.readOneCompanyType(
                        year, evaluationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/evaluations-company/{evaluationId}")
    @Operation(summary = "회사 설문조사 한 개 수정",
            description = "로그인한 사용자가 만든 회사 설문조사를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회사 설문조사 한 개 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    EvaluationsResponseDto.UpdateOneForCompanyDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EvaluationsResponseDto.UpdateOneForCompanyDto> updateOneCompanyType(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.UpdateOneForCompanyDto requestDto){
        EvaluationsResponseDto.UpdateOneForCompanyDto responseDto
                = evaluationsServiceFacade.updateOneCompanyType(
                        evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/evaluations/{evaluationId}")
    @Operation(summary = "회사 설문조사 한 개 삭제",
            description = "로그인한 사용자가 만든 설문조사를 삭제할 수 있다.")
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
