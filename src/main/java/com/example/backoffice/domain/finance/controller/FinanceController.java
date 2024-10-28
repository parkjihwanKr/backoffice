package com.example.backoffice.domain.finance.controller;

import com.example.backoffice.domain.finance.dto.FinanceRequestDto;
import com.example.backoffice.domain.finance.dto.FinanceResponseDto;
import com.example.backoffice.domain.finance.service.FinanceServiceV1;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FinanceController {

    private final FinanceServiceV1 financeService;

    // 회사 잔고 조회
    @GetMapping("/finance/{financeId}")
    public ResponseEntity<FinanceResponseDto.ReadOneDto> readOne(
            @PathVariable Long financeId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        FinanceResponseDto.ReadOneDto responseDto =
                financeService.readOne(financeId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 월별 잔고 이력 조회

    // 회사 부서 예산 할당
    @PostMapping("/finance")
    public ResponseEntity<FinanceResponseDto.CreateDepartmentBudgetDto> createDepartmentBudget(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody FinanceRequestDto.CreateDepartmentBudgetDto requestDto){
        FinanceResponseDto.CreateDepartmentBudgetDto responseDto
                = financeService.createDepartmentBudget(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 회사 부서 지출 내역서에 근거한 부서 계좌 송금

    // 회사 부서 지출 내역서에 근거한 부서 계좌 송금 취소

    // 회사 멤버 급여 송금

    // 회사 멤버 급여 송금 취소
}
