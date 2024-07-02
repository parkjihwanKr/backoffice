package com.example.backoffice.domain.answer.converter;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.question.entity.Questions;

public class AnswersConverter {

    public static Answers toEntity(Questions question, String answerText){
        return Answers.builder()
                .question(question)
                .answerText(answerText)

                .build();
    }
}
