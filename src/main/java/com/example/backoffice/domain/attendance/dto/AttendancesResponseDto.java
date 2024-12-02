package com.example.backoffice.domain.attendance.dto;

import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class AttendancesResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCheckInTimeDto{
        private Long attendanceId;
        private String memberName;
        private LocalDateTime checkInTime;
        private AttendanceStatus attendanceStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCheckOutTimeDto{
        private Long attendanceId;
        private String memberName;
        private LocalDateTime checkInTime;
        private LocalDateTime checkOutTime;
        private AttendanceStatus attendanceStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadDto {
        private List<ReadOneDto> readOneDtoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadOneDto {
        private Long attendanceId;
        private Long memberId;
        private String memberName;
        private LocalDateTime checkInTime;
        private LocalDateTime checkOutTime;
        private AttendanceStatus attendanceStatus;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAttendancesStatusDto {
        private Long attendanceId;
        private String memberName;
        private AttendanceStatus attendanceStatus;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto{
        private String memberName;
        private AttendanceStatus attendanceStatus;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadMonthlyDto {
        private LocalDateTime createdAt;
        private Integer absentCount;
        private Integer onTimeCount;
        private Integer onVacationCount;
        private Integer outOfOfficeCount;
        private Integer lateCount;
        private Integer halfDayCount;
        private Integer holidayCount;
    }
}
