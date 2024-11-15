package com.example.backoffice.domain.attendance.dto;

import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AttendancesResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto{
        private String memberName;
        private AttendanceStatus attendanceStatus;
    }
}
