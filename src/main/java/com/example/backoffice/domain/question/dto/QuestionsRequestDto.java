package com.example.backoffice.domain.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class QuestionsRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsRequestDto.CreateAllDto",
            description = "질문 리스트 생성 요청 DTO")
    public static class CreateAllDto {
        private List<CreateOneDto> questionList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsRequestDto.CreateOneDto",
            description = "질문 하나 생성 요청 DTO")
    public static class CreateOneDto {
        // 1. 객관식, 2. 주관식
        private String questionType;
        // ex) 당신은 회사 생활에 만족하십니까?
        // ex) 당신이 회사 생활에 불만족하는 이유를 서술해주세요.
        private String questionText;
        // 1 선택 시, shortAnswerDto -> null, 2 선택 시, multipleChoiceAnswer -> null
        private List<String> multipleChoiceAnswerList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsRequestDto.UpdateOneDto",
            description = "질문 하나 수정 요청 DTO")
    public static class UpdateOneDto{
        private String changeQuestionText;
        private String changeQuestionType;
        private List<String> multipleChoiceAnswerList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsRequestDto.UpdateOneForOrderDto",
            description = "질문 순서 수정 요청 DTO")
    public static class UpdateOneForOrderDto {
        private Long updatedNumber;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsRequestDto.DeleteDto",
            description = "질문 삭제 요청 DTO")
    public static class DeleteDto{
        private List<Long> questionNumberList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "QuestionsRequestDto.SubmitOneDto",
            description = "설문조사 작성자가 질문 하나 제출 요청 DTO")
    public static class SubmitOneDto {
        private Long questionNumber;
        private String shortAnswer;
        private List<Long> multipleChoiceAnswerNumber;
    }
}
