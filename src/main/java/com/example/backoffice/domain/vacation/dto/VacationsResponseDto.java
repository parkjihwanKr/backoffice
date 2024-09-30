package com.example.backoffice.domain.vacation.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class VacationsResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOneDto {
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadMonthForDepartmentDto {

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadDayDto {
        private Long eventId;
        private String title;
        private String reason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadMonthDto {
        private Long eventId;
        private String title;
        private String reason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneDto {
        private Long eventId;
        private String title;
        private String reason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }
}
