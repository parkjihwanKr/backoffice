package com.example.backoffice.domain.evaluation.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "EvaluationsResponseDto.CreateOneForDepartmentDto",
            description = "부서 타입의 설문조사 생성 응답 DTO")
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
    @Schema(name = "EvaluationsResponseDto.ReadOneForDepartmentDto",
            description = "부서 타입의 설문조사 조회 응답 DTO")
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
    @Schema(name = "EvaluationsResponseDto.UpdateOneForDepartmentDto",
            description = "부서 타입의 설문조사 수정 응답 DTO")
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
    @Schema(name = "EvaluationsResponseDto.CreateOneForCompanyDto",
            description = "회사 타입의 설문조사 생성 응답 DTO")
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
    @Schema(name = "EvaluationsResponseDto.ReadOneForCompanyDto",
            description = "회사 타입의 설문조사 조회 응답 DTO")
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
    @Schema(name = "EvaluationsResponseDto.UpdateOneForCompanyDto",
            description = "회사 타입의 설문조사 수정 응답 DTO")
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
    @Schema(name = "EvaluationsResponseDto.SubmitOneDto",
            description = "설문조사 제출 응답 DTO")
    public static class SubmitOneDto {
        private Long evaluationId;
        private String submitterName;
        // 제출 시간
        private LocalDateTime createdAt;
    }
}
