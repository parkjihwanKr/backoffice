package com.example.backoffice.domain.memberAnswer.service;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.question.entity.Questions;

import java.util.List;

public interface MembersAnswersServiceV1 {
    void save(Questions question, Members loginMember, List<Answers> answersList);
    void saveOne(Questions question, Members loginMember, Answers answer);
}
