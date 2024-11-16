package com.example.backoffice.domain.attendance.controller;

import com.example.backoffice.domain.attendance.dto.AttendancesRequestDto;
import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
