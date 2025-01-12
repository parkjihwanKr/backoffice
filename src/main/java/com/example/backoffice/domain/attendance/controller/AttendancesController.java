package com.example.backoffice.domain.attendance.controller;

import com.example.backoffice.domain.attendance.dto.AttendancesRequestDto;
import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
import com.example.backoffice.global.common.DateRange;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AttendancesController {

    private final AttendancesServiceV1 attendancesService;

    @PatchMapping("/attendances/{attendanceId}/check-in")
    public ResponseEntity<AttendancesResponseDto.UpdateCheckInTimeDto> updateCheckInTimeForMember(
            @PathVariable Long attendanceId,
            @RequestBody AttendancesRequestDto.UpdateCheckInTimeDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.UpdateCheckInTimeDto responseDto
                = attendancesService.updateCheckInTimeForMember(
                attendanceId, requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/attendances/{attendanceId}/check-out")
    public ResponseEntity<AttendancesResponseDto.UpdateCheckOutTimeDto> updateCheckOutTimeForMember(
            @PathVariable Long attendanceId,
            @RequestBody AttendancesRequestDto.UpdateCheckOutTimeDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.UpdateCheckOutTimeDto responseDto
                = attendancesService.updateCheckOutTimeForMember(
                attendanceId, requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/members/{memberId}/attendances")
    public ResponseEntity<List<AttendancesResponseDto.ReadOneDto>> readFilteredForMember(
            @PathVariable Long memberId,
            @RequestParam(name = "year", required = false) Long year,
            @RequestParam(name = "month", required = false) Long month,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<AttendancesResponseDto.ReadOneDto> responseDtoList
                = attendancesService.readFilteredForMember(
                memberId, year, month, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @GetMapping("/attendances/{attendanceId}")
    public ResponseEntity<AttendancesResponseDto.ReadOneDto> readOne(
            @PathVariable Long attendanceId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.ReadOneDto responseDto
                = attendancesService.readOne(attendanceId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/attendances")
    public ResponseEntity<Page<AttendancesResponseDto.ReadOneDto>> readFilteredForAdmin(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestParam(name = "memberName",required = false) String memberName,
            @RequestParam(name = "attendanceStatus", required = false) String attendanceStatus,
            @RequestParam(name = "checkInRange", required = false) @Valid DateRange checkInRange,
            @RequestParam(name = "checkOutRange", required = false) @Valid DateRange checkOutRange,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC, size = 7)Pageable pageable){
        Page<AttendancesResponseDto.ReadOneDto> responseDtoPage
                = attendancesService.readForAdmin(
                memberName, attendanceStatus, checkInRange,
                checkOutRange, memberDetails.getMembers(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoPage);
    }

    @GetMapping("/admin/attendances/monthly")
    public ResponseEntity<Page<AttendancesResponseDto.ReadMonthlyDto>> readFilteredByMonthlyForAdmin(
            @RequestParam(name = "department", required = false) String department,
            @RequestParam(name = "year") Long year, @RequestParam(name = "month") Long month,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        Page<AttendancesResponseDto.ReadMonthlyDto> responseDtoPage
                = attendancesService.readFilteredByMonthlyForAdmin(
                department, year, month, pageable, memberDetails.getMembers());

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoPage);
    }

    @GetMapping("/admin/attendances/daily")
    public ResponseEntity<Page<AttendancesResponseDto.ReadOneDto>> readFilteredByDailyForAdmin(
            @RequestParam(name = "department", required = false) String department,
            @RequestParam(name = "memeberName", required = false) String memberName,
            @RequestParam(name = "year") Long year, @RequestParam(name = "month")Long month,
            @RequestParam(name = "day") Long day,
            @PageableDefault(sort = "memberId", direction = Sort.Direction.ASC, size = 20) Pageable pageable,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        Page<AttendancesResponseDto.ReadOneDto> responseDtoPage
                = attendancesService.readFilteredByDailyForAdmin(
                department, memberName, year, month,
                day, pageable, memberDetails.getMembers());

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoPage);
    }

    @PatchMapping("/members/{memberId}/attendances/{attendanceId}/status")
    public ResponseEntity<AttendancesResponseDto.UpdateAttendancesStatusDto> updateOneStatusForAdmin(
            @PathVariable Long memberId, @PathVariable Long attendanceId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody AttendancesRequestDto.UpdateAttendanceStatusDto requestDto){
        AttendancesResponseDto.UpdateAttendancesStatusDto responseDto
                = attendancesService.updateOneStatusForAdmin(
                        memberId, attendanceId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/attendances")
    public ResponseEntity<AttendancesResponseDto.CreateOneDto> createOneForAdmin(
            @RequestBody AttendancesRequestDto.CreateOneDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.CreateOneDto responseDto
                = attendancesService.createOneForAdmin(
                requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/attendances")
    public ResponseEntity<CommonResponseDto<Void>> deleteForAdmin(
            @RequestBody List<Long> deleteAttendanceIdList,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        attendancesService.deleteForAdmin(
                deleteAttendanceIdList, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "해당 근태 기록들이 삭제되었습니다.", 200
                )
        );
    }

    @GetMapping("/admin/attendances")
    public ResponseEntity<List<AttendancesResponseDto.ReadScheduledRecordDto>> readScheduledRecord(
            @RequestParam(name = "department", required = false) String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<AttendancesResponseDto.ReadScheduledRecordDto> responseDtoList
                = attendancesService.readScheduledRecord(department, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @GetMapping("/members/{memberId}/check-today-attendance")
    public ResponseEntity<AttendancesResponseDto.ReadTodayOneDto> readTodayOne (
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.ReadTodayOneDto responseDto
                = attendancesService.readTodayOne(memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
