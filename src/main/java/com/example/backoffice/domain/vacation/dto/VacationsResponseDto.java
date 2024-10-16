package com.example.backoffice.domain.vacation.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
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
        private Boolean isAccepted;
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
        private Boolean isAccepted;
        private MemberDepartment department;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
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

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForAdminDto {
        private Long vacationId;
        private String acceptedVacationMemberName;
        private Boolean isAccepted;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneIsAcceptedDto {
        private Long vacationId;
        private String onVacationMemberName;
        private Boolean isAccepted;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }
}
