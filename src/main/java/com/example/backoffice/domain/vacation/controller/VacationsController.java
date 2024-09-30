package com.example.backoffice.domain.vacation.controller;

import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VacationsController {

    private final VacationsServiceV1 vacationsService;

    // 멤버 개인 휴가 생성
    @PostMapping("/vacations")
    public ResponseEntity<CommonResponseDto<VacationsResponseDto.CreateOneDto>> createOne(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody VacationsRequestDto.CreateOneDto requestDto){
        VacationsResponseDto.CreateOneDto responseDto =
                vacationsService.createOne(memberDetails.getMembers(), requestDto);
        String message = "해당 사항은 검토 후, 사내 알림으로 알려드리겠습니다.";
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        responseDto, message, 200));
    }

    // 해당 날짜 달력 클릭 시, 휴가 나가 있는 인원 조회
    @GetMapping("/vacations/departments/{department}/years/{year}/month/{month}/days/{day}")
    public ResponseEntity<List<VacationsResponseDto.ReadDayDto>> readDayForDepartment(
            @PathVariable String department, @PathVariable Long year,
            @PathVariable Long month, @PathVariable Long day,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<VacationsResponseDto.ReadDayDto> responseDtoList
                = vacationsService.readDay(department, year, month, day, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 부서 휴가 일정 조회
    @GetMapping("/vacations/departments/{department}/years/{year}/months/{month}")
    public ResponseEntity<List<VacationsResponseDto.ReadMonthForDepartmentDto>> readMonthForDepartment(
            @PathVariable String department,
            @PathVariable Long year, @PathVariable Long month,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<VacationsResponseDto.ReadMonthForDepartmentDto> responseDtoList =
                vacationsService.readMonthForDepartment(
                        department, year, month, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 개인 휴가 일정 부분 수정
    @PatchMapping("/vacations/{vacationId}")
    public ResponseEntity<VacationsResponseDto.UpdateOneDto> updateOne(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody VacationsRequestDto.UpdateOneDto requestDto){
        VacationsResponseDto.UpdateOneDto responseDto
                = vacationsService.updateOne(vacationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 개인 휴가 일정 부분 삭제
    @DeleteMapping("/vacations/{vacationId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteOne(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        vacationsService.deleteOne(vacationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "휴가 등록 취소 성공", 200
                )
        );
    }
}
