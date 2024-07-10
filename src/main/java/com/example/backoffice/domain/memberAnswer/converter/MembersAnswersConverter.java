package com.example.backoffice.domain.memberAnswer.converter;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.memberAnswer.entity.MembersAnswers;
import com.example.backoffice.domain.question.entity.Questions;

public class MembersAnswersConverter {

    public static MembersAnswers toEntity(Questions question, Members loginMember, Answers answer){
        return MembersAnswers.builder()
                .question(question)
                .member(loginMember)
                .answer(answer)
                .build();
    }
}
