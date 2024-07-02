package com.example.backoffice.domain.question.dto;

import jakarta.validation.constraints.NotNull;
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
    public static class CreateAllDto {
        private List<CreateOneDto> questionList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOneDto {
        // 1. 객관식, 2. 주관식
        private String questionType;
        // ex) 당신은 회사 생활에 만족하십니까?
        private String questionText;
        // 1 선택 시, shortAnswerDto -> null, 2 선택 시, multipleChoiceAnswer -> null
        private List<String> multipleChoiceAnswerList;
        // questionType을 주관식을 선택한 경우에 작성 가능
        private String shortAnswer;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneDto{
        private Long beforeQuestionNumber;
        private Long afterQuestionNumber;
        private String changeQuestionText;
        private String changeQuestionType;
        private List<String> multipleChoiceAnswerList;
    }
}
