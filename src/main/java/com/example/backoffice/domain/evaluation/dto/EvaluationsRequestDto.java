package com.example.backoffice.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class EvaluationsRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOneForDepartmentDto{
        private String description;
        private String department;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }
}
