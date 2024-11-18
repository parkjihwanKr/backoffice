package com.example.backoffice.domain.attendance.controller;

import com.example.backoffice.domain.attendance.dto.AttendancesRequestDto;
import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
import com.example.backoffice.global.common.DateRange;
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
    public ResponseEntity<AttendancesResponseDto.UpdateCheckInTimeDto> updateCheckInTime(
            @PathVariable Long attendanceId,
            @RequestBody AttendancesRequestDto.UpdateCheckInTimeDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.UpdateCheckInTimeDto responseDto
                = attendancesService.updateCheckInTime(
                        attendanceId, requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/attendances/{attendanceId}/check-out")
    public ResponseEntity<AttendancesResponseDto.UpdateCheckOutTimeDto> updateCheckOutTime(
            @PathVariable Long attendanceId,
            @RequestBody AttendancesRequestDto.UpdateCheckOutTimeDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.UpdateCheckOutTimeDto responseDto
                = attendancesService.updateCheckOutTime(
                attendanceId, requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/members/{memberId}/attendances")
    public ResponseEntity<List<AttendancesResponseDto.ReadOneDto>> readFiltered(
            @PathVariable Long memberId,
            @RequestParam(required = false) Long year,
            @RequestParam(required = false) Long month,
            @RequestParam(required = false) String attendanceStatus,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<AttendancesResponseDto.ReadOneDto> responseDtoList
                = attendancesService.readFiltered(
                        memberId, year, month, attendanceStatus, memberDetails.getMembers());
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
    public ResponseEntity<Page<AttendancesResponseDto.ReadOneDto>> readForAdmin(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestParam(required = false) String memberName,
            @RequestParam(required = false) String attendanceStatus,
            @RequestParam(required = false) @Valid DateRange checkInRange,
            @RequestParam(required = false) @Valid DateRange checkOutRange,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC)Pageable pageable){
        Page<AttendancesResponseDto.ReadOneDto> responseDtoPage
                = attendancesService.readForAdmin(
                        memberName, attendanceStatus, checkInRange,
                checkOutRange, memberDetails.getMembers(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoPage);
    }

    @PatchMapping("/members/{memberId}/attendances/{attendanceId}/status")
    public ResponseEntity<AttendancesResponseDto.UpdateAttendancesStatusDto> updateOneStatusForAdmin(
            @PathVariable Long memberId, @PathVariable Long attendanceId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody AttendancesRequestDto.UpdateAttendanceStatusDto requestDto){
        AttendancesResponseDto.UpdateAttendancesStatusDto responseDto
                = attendancesService.updateOneStatusForAdmin(memberId, attendanceId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
