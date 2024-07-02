package com.example.backoffice.domain.question.entity;

import lombok.Getter;

@Getter
public enum QuestionsType {

    MULTIPLE_CHOICE_ANSWER(AnswerType.MULTIPLE_CHOICE_ANSWER),
    SHORT_ANSWER(AnswerType.SHORT_ANSWER);

    private final String answerType;
    QuestionsType(String answerType){
        this.answerType = answerType;
    }

    public static class AnswerType {
        public static final String MULTIPLE_CHOICE_ANSWER = "MULTIPLE_CHOICE_ANSWER";
        public static final String SHORT_ANSWER = "SHORT_ANSWER";

    }
}
