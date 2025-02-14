package com.example.backoffice.domain.evaluation.dto;

import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class EvaluationsRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "EvaluationsRequestDto.CreateOneForDepartmentDto",
            description = "설문조사 생성 요청 DTO")
    public static class CreateOneDto{
        private String title;
        private String description;
        private String department;
        private String evaluationType;
        private String startDate;
        private String endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "EvaluationsRequestDto.UpdateOneForDepartmentDto",
            description = "부서 타입의 설문조사 생성 요청 DTO")
    public static class UpdateOneDto {
        private String title;
        private String description;
        private String startDate;
        private String endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "EvaluationsRequestDto.SubmitOneDto",
            description = "설문조사 제출 요청 DTO")
    public static class SubmitOneDto {
        // 자기 자신이 설문조사를 진행했다는 것에 동의하십니까?
        private Boolean checked;
        private List<QuestionsRequestDto.SubmitOneDto> questionSubmitAllDto;
    }
}
