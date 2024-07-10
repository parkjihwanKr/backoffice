package com.example.backoffice.domain.evaluation.dto;

import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubmitOneDto {
        // 자기 자신이 설문조사를 진행했다는 것에 동의하십니까?
        private Boolean checked;
        private List<QuestionsRequestDto.SubmitOneDto> questionSubmitAllDto;
    }
}
