package com.example.backoffice.domain.expense.controller;

import com.example.backoffice.domain.expense.dto.ExpenseRequestDto;
import com.example.backoffice.domain.expense.dto.ExpenseResponseDto;
import com.example.backoffice.domain.expense.service.ExpenseServiceV1;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ExpenseController {

    private final ExpenseServiceV1 expenseService;

    // 부서 지출 내역서 제출
    @PostMapping("/departments/{department}/expense")
    public ResponseEntity<CommonResponseDto<ExpenseResponseDto.CreateOneDto>> createOne(
            @PathVariable String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(name = "data")ExpenseRequestDto.CreateOneDto requestDto,
            @RequestPart(name = "files")List<MultipartFile> fileList){
        ExpenseResponseDto.CreateOneDto responseDto
                = expenseService.createOne(
                        department, memberDetails.getMembers(), requestDto, fileList);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        responseDto, "관리자 검토 후, 알려드리겠습니다.", 200));
    }
    // 관리자 부서 지출 내역서 승인
    @PatchMapping("/departments/{department}/expense/{expenseId}/process")
    public ResponseEntity<ExpenseResponseDto.UpdateOneForProcessDto> updateOneForProcess(
            @PathVariable Long expenseId,
            @PathVariable String department,
            @RequestParam String process,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        ExpenseResponseDto.UpdateOneForProcessDto responseDto
                = expenseService.updateOneForProcess(expenseId, department, process, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 지출 내역서 상세보기
    @GetMapping("/expense/{expenseId}")
    public ResponseEntity<ExpenseResponseDto.ReadOneDto> readOne(
            @PathVariable Long expenseId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        ExpenseResponseDto.ReadOneDto responseDto
                = expenseService.readOne(expenseId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 지출 내역서 리스트 조회
    @GetMapping("/expense")
    public ResponseEntity<Page<ExpenseResponseDto.ReadOneDto>> readFiltered(
            @ModelAttribute ExpenseRequestDto.ReadFilteredDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.ASC) Pageable pageable){
        Page<ExpenseResponseDto.ReadOneDto> responsePage
                = expenseService.readFiltered(requestDto, memberDetails.getMembers(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responsePage);
    }

    // 부서 지출 내역서 수정

    // 부서 지출 내역서 삭제


}
