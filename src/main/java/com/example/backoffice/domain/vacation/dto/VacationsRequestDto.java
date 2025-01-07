package com.example.backoffice.domain.vacation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VacationsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePeriodDto {
        private String startDate;
        private String endDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto {
        private String title;
        @NotNull
        private String startDate;
        @NotNull
        private String endDate;
        @NotNull
        private Boolean urgent;
        // urgent를 true로 체크한 사람에 한정해서 사유를 적어야함.
        private String urgentReason;
        private String vacationType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneDto {
        private String title;
        @NotNull
        private String startDate;
        @NotNull
        private String endDate;
        private Boolean urgent;
        private String urgentReason;
        private String vacationType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteOneByAdminDto {
        private String reason;
    }
}
