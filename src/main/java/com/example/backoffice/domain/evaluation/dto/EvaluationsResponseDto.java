package com.example.backoffice.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EvaluationsResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOneForDepartmentDto {
        private String title;
        private String description;
        // 만든이
        private String writerName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOneForCompanyDto {
        private String title;
        private String description;
        private String writerName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneForDepartmentDto {
        private String title;
        private Integer year;
        private Integer quarter;
        private String writerName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneForCompanyDto {
        private String memberName;
        private String title;
    }
}
