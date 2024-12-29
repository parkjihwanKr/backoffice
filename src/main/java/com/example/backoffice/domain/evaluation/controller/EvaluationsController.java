package com.example.backoffice.domain.evaluation.controller;

import com.example.backoffice.domain.evaluation.dto.EvaluationsRequestDto;
import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.facade.EvaluationsServiceFacadeV1;
import com.example.backoffice.domain.evaluation.service.EvaluationsServiceV1;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EvaluationsController {

    private final EvaluationsServiceFacadeV1 evaluationsServiceFacade;

    @PostMapping("/evaluations-department")
    public ResponseEntity<EvaluationsResponseDto.CreateOneForDepartmentDto> createOneForDepartment(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.CreateOneForDepartmentDto requestDto){
        EvaluationsResponseDto.CreateOneForDepartmentDto responseDto
                = evaluationsServiceFacade.createOneForDepartment(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/evaluations-company")
    public ResponseEntity<EvaluationsResponseDto.CreateOneForCompanyDto> createOneForCompany(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.CreateOneForCompanyDto requestDto){
        EvaluationsResponseDto.CreateOneForCompanyDto responseDto
                = evaluationsServiceFacade.createOneForCompany(
                        memberDetails.getMembers(),requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // front 해당 부서에 대한 설문조사를 시행하시겠습니까? -> y/n
    // 시행 기간은 일괄적으로 정해져 있는데?
    @GetMapping("/evaluations-department/{evaluationId}")
    public ResponseEntity<EvaluationsResponseDto.ReadOneForDepartmentDto> readOneForDepartment(
            @RequestParam(name = "year")Integer year, @RequestParam(name = "quarter")Integer quarter,
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        EvaluationsResponseDto.ReadOneForDepartmentDto responseDto
                = evaluationsServiceFacade.readOneForDepartment(
                        year, quarter, evaluationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/evaluations-company/{evaluationId}")
    public ResponseEntity<EvaluationsResponseDto.ReadOneForCompanyDto> readOneForCompany(
            @RequestParam(name = "year") Integer year, @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        EvaluationsResponseDto.ReadOneForCompanyDto responseDto
                = evaluationsServiceFacade.readOneForCompany(
                        year, evaluationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/evaluations-department/{evaluationId}")
    public ResponseEntity<EvaluationsResponseDto.UpdateOneForDepartmentDto> updateOneForDepartment(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.UpdateOneForDepartmentDto requestDto){
        EvaluationsResponseDto.UpdateOneForDepartmentDto responseDto
                = evaluationsServiceFacade.updateOneForDepartment(
                        evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/evaluations-company/{evaluationId}")
    public ResponseEntity<EvaluationsResponseDto.UpdateOneForCompanyDto> updateOneForCompany(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.UpdateOneForCompanyDto requestDto){
        EvaluationsResponseDto.UpdateOneForCompanyDto responseDto
                = evaluationsServiceFacade.updateOneForCompany(
                        evaluationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/evaluations/{evaluationId}")
    public ResponseEntity<CommonResponse<Void>> deleteOne(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        evaluationsServiceFacade.deleteOne(evaluationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(HttpStatus.OK, "해당 설문 조사는 삭제되었습니다.", null));
    }

    @PostMapping("/evaluations/{evaluationId}/submit")
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
