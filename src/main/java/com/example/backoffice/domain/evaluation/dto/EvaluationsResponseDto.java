package com.example.backoffice.domain.evaluation.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.entity.Questions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EvaluationsResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOneForDepartmentDto {
        private Long evaluationId;
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
        private Long evaluationId;
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
        private Long evaluationId;
        private String writerName;
        private String title;
        private String description;
        private Integer year;
        private Integer quarter;
        private List<QuestionsResponseDto.ReadOneDto> questionList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneForCompanyDto {
        private Long evaluationId;
        private String writerName;
        private String title;
        private String description;
        private Integer year;
        private List<QuestionsResponseDto.ReadOneDto> questionList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForDepartmentDto {
        private Long evaluationId;
        private MemberDepartment department;
        private String writerName;
        private String title;
        private String description;
        private Integer year;
        private Integer quarter;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForCompanyDto {
        private Long evaluationId;
        private String writerName;
        private String title;
        private String description;
        private Integer year;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubmitOneDto {
        private Long evaluationId;
        private String submitterName;
        // 제출 시간
        private LocalDateTime createdAt;
    }
}
