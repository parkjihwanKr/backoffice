package com.example.backoffice.domain.memberAnswer.service;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.memberAnswer.converter.MembersAnswersConverter;
import com.example.backoffice.domain.memberAnswer.entity.MembersAnswers;
import com.example.backoffice.domain.memberAnswer.repository.MembersAnswersRepository;
import com.example.backoffice.domain.question.entity.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembersAnswersServiceV1Impl implements MembersAnswersServiceV1{

    private final MembersAnswersRepository membersAnswersRepository;

    @Override
    @Transactional
    public void save(Questions question, Members loginMember, List<Answers> answersList){
        for(Answers answer : answersList){
            MembersAnswers memberAnswer
                    = MembersAnswersConverter.toEntity(question, loginMember, answer);
            membersAnswersRepository.save(memberAnswer);
        }
    }
    @Override
    @Transactional
    public void saveOne(Questions question, Members loginMember, Answers answer){
        MembersAnswers memberAnswer
                = MembersAnswersConverter.toEntity(question, loginMember, answer);
        membersAnswersRepository.save(memberAnswer);
    }
}
