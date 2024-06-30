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
        private String questionText;
        private String questionType;
        private List<String> multipleChoiceAnswerList;
    }
}
