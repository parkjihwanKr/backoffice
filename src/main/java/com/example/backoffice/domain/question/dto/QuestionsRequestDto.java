package com.example.backoffice.domain.question.dto;

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
        // ex) 당신이 회사 생활에 불만족하는 이유를 서술해주세요.
        private String questionText;
        // 1 선택 시, shortAnswerDto -> null, 2 선택 시, multipleChoiceAnswer -> null
        private List<String> multipleChoiceAnswerList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneDto{
        private String changeQuestionText;
        private String changeQuestionType;
        private List<String> multipleChoiceAnswerList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForOrderDto {
        private Long updatedNumber;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteDto{
        private List<Long> questionNumberList;
    }
}
