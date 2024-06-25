package com.example.backoffice.domain.evaluation.controller;

import com.example.backoffice.domain.evaluation.dto.EvaluationsRequestDto;
import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.service.EvaluationsServiceV1;
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

    private final EvaluationsServiceV1 evaluationsService;

    @PostMapping("/evaluations")
    public ResponseEntity<EvaluationsResponseDto.CreateOneForDepartmentDto> createOneForDepartment(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EvaluationsRequestDto.CreateOneForDepartmentDto requestDto){
        EvaluationsResponseDto.CreateOneForDepartmentDto responseDto
                = evaluationsService.createOneForDepartment(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // front 해당 부서에 대한 설문조사를 시행하시겠습니까? -> y/n
    // 시행 기간은 일괄적으로 정해져 있는데?
    @GetMapping("/evaluations/{evaluationId}/department")
    public ResponseEntity<EvaluationsResponseDto.ReadOneForDepartmentDto> readOneForDepartment(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        EvaluationsResponseDto.ReadOneForDepartmentDto responseDto
                = evaluationsService.readOneForDepartment(evaluationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/evaluations/{evaluationId}/company")
    public ResponseEntity<EvaluationsResponseDto.ReadOneForCompanyDto> readOneForCompany(
            @PathVariable Long evaluationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return null;
    }
}
