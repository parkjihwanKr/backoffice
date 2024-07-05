package com.example.backoffice.domain.evaluation.dto;

import com.example.backoffice.domain.question.entity.Questions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
        private String writerName;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneForDepartmentDto {
        private String writerName;
        private String title;
        private Integer year;
        private Integer quarter;
        private List<Questions> evaluationQuestionList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneForCompanyDto {
        private String writerName;
        private String title;
        private String description;
        private Integer year;
        private List<Questions> evaluationQuestionList;
    }
}
