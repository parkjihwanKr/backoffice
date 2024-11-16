package com.example.backoffice.domain.attendance.dto;

import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AttendancesResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCheckInTimeDto{
        private String memberName;
        private LocalDateTime checkInTime;
        private AttendanceStatus attendanceStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCheckOutTimeDto{
        private String memberName;
        private LocalDateTime checkInTime;
        private LocalDateTime checkOutTime;
        private AttendanceStatus attendanceStatus;
    }
}
