package com.example.backoffice.domain.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class AttendancesRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCheckInTimeDto{
        private String checkInTime;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCheckOutTimeDto{
        private String checkOutTime;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAttendanceStatusDto {
        private String attendanceStatus; // 변경하려는 근태 상태 (e.g., "ON_TIME", "LATE")
        private String description; // 근태 상태 변경 사유
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto {
        private Long memberId;
        private String memberName;
        private String attendanceStatus;
        private String startDate;
        private String endDate;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneManuallyForAdminDto {
        private String memberName;
        private String attendanceStatus;
        private String checkInTime;
        private String checkOutTime;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteForAdminDto{
        private List<Long> deleteAttendanceIdList;
    }
}
