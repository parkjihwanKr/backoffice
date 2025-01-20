package com.example.backoffice.domain.answer.converter;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.question.entity.Questions;

public class AnswersConverter {

    public static Answers toEntity(Questions question, String answerText, Long number){
        return Answers.builder()
                .question(question)
                .text(answerText)
                .number(number)
                .build();
    }
}
