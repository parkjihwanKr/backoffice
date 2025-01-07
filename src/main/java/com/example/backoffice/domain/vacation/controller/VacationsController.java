package com.example.backoffice.domain.vacation.controller;

import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.facade.VacationsServiceFacadeV1;
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

    private final VacationsServiceFacadeV1 vacationsServiceFacade;

    // 이례적인 휴가 신청 날짜 추가 (= 회사 내부의 사정이 생겼을 때 / 기존에 신청하던 날짜가 추석 일때)
    @PatchMapping("/vacations/update-period")
    public ResponseEntity<CommonResponseDto<VacationsResponseDto.UpdatePeriodDto>> updatePeriodByAdmin(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody VacationsRequestDto.UpdatePeriodDto requestDto){
        VacationsResponseDto.UpdatePeriodDto responseDto
                = vacationsServiceFacade.updatePeriodByAdmin(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        responseDto, "성공적으로 휴가 신청 기간이 변경되었습니다.", 200));
    }

    // 멤버 개인 휴가 생성
    @PostMapping("/vacations")
    public ResponseEntity<CommonResponseDto<VacationsResponseDto.CreateOneDto>> createOneByMember(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody VacationsRequestDto.CreateOneDto requestDto){
        VacationsResponseDto.CreateOneDto responseDto =
                vacationsServiceFacade.createOneByMember(memberDetails.getMembers(), requestDto);
        String message = "해당 사항은 검토 후, 사내 알림으로 알려드리겠습니다.";
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        responseDto, message, 200));
    }

    // readDay, readDayForAdmin 없음.
    // 관리자가 아닌 자기 자신의 휴가 일정을 조회
    @GetMapping("/vacations/{vacationId}")
    public ResponseEntity<VacationsResponseDto.ReadDayDto> readDayByMember(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        VacationsResponseDto.ReadDayDto responseDto
                = vacationsServiceFacade.readDayByMember(vacationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 개인 휴가 일정 부분 수정
    @PatchMapping("/vacations/{vacationId}")
    public ResponseEntity<VacationsResponseDto.UpdateOneDto> updateOneByMember(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody VacationsRequestDto.UpdateOneDto requestDto){
        VacationsResponseDto.UpdateOneDto responseDto
                = vacationsServiceFacade.updateOneByMember(vacationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 회사원들의 휴가를 적용 여부를 변경
    @PatchMapping("/admin/vacations/{vacationId}")
    public ResponseEntity<CommonResponseDto<VacationsResponseDto.UpdateOneByAdminDto>> updateOneByAdmin(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        VacationsResponseDto.UpdateOneByAdminDto responseDto
                = vacationsServiceFacade.updateOneByAdmin(vacationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        responseDto, "승인 요청이 되었습니다.", 200
                )
        );
    }

    // 개인 휴가 일정 부분 삭제
    @DeleteMapping("/vacations/{vacationId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteOneByMember(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        vacationsServiceFacade.deleteOneByMember(vacationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "휴가 등록 취소 성공", 200
                )
        );
    }

    // 특정 달의 필터링된 휴가 상황 모두 조회
    // readByHrManager
    @GetMapping("/vacations/years/{year}/months/{month}/filtered")
    public ResponseEntity<List<VacationsResponseDto.ReadMonthDto>> readByHrManager(
            @PathVariable Long year, @PathVariable Long month,
            @RequestParam(name = "isAccepted", required = false) Boolean isAccepted,
            @RequestParam(name = "urgent", required = false) Boolean urgent,
            @RequestParam(name = "department", required = false) String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<VacationsResponseDto.ReadMonthDto> responseDtoList
                = vacationsServiceFacade.readByHrManager(
                        year, month, isAccepted, urgent, department, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @DeleteMapping("/admin/vacations/{vacationId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteOneByHrManager(
            @PathVariable Long vacationId,
            @RequestBody VacationsRequestDto.DeleteOneByAdminDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        vacationsServiceFacade.deleteOneByHrManager(
                vacationId, requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "성공적으로 휴가 요청이 삭제되었습니다.", 200
                )
        );
    }
}
