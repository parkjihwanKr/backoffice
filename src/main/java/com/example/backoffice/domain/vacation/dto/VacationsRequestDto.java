package com.example.backoffice.domain.vacation.dto;

import com.example.backoffice.domain.event.exception.annotation.UrgentReasonRequired;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class VacationsRequestDto {

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
        private String reason;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @UrgentReasonRequired
    public static class UpdateOneDto {
        private String title;
        @NotNull
        private String startDate;
        @NotNull
        private String endDate;
        @NotNull
        private Boolean urgent;
        private String reason;
    }
}
