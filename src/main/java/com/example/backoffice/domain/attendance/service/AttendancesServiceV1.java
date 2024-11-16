package com.example.backoffice.domain.attendance.service;

import com.example.backoffice.domain.attendance.dto.AttendancesRequestDto;
import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.member.entity.Members;

public interface AttendancesServiceV1 {
    void create(Boolean isWeekDay);

    AttendancesResponseDto.UpdateCheckInTimeDto updateCheckInTime(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckInTimeDto requestDto,
            Members loginMember);

    AttendancesResponseDto.UpdateCheckOutTimeDto updateCheckOutTime(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckOutTimeDto requestDto,
            Members loginMember);
}
