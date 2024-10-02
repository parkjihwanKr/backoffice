package com.example.backoffice.domain.vacation.dto;

import com.example.backoffice.domain.vacation.entity.VacationType;
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
    public static class UpdatePeriodDto {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOneDto {
        private String title;
        private VacationType vacationType;
        private String urgentReason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadDayDto {
        private Long vacationId;
        private String onVacationMemberName;
        private String title;
        private String urgentReason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private VacationType vacationType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadMonthDto {
        private Long vacationId;
        private String onVacationMemberName;
        private String title;
        private String urgentReason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private VacationType vacationType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneDto {
        private Long vacationId;
        private String title;
        private String urgentReason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private VacationType vacationType;
    }
}
