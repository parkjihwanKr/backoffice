package com.example.backoffice.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class EvaluationsRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOneForDepartmentDto{
        private String title;
        private String description;
        private String department;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOneForCompanyDto {
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForDepartmentDto {
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForCompanyDto {
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
