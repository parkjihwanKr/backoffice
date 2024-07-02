package com.example.backoffice.domain.answer.service;

import com.example.backoffice.domain.answer.converter.AnswersConverter;
import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.answer.exception.AnswersCustomException;
import com.example.backoffice.domain.answer.exception.AnswersExceptionCode;
import com.example.backoffice.domain.answer.repository.AnswersRepository;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.entity.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswersServiceImplV1 implements AnswersServiceV1{

    private final AnswersRepository answersRepository;

    @Override
    @Transactional
    public List<Answers> createAll(
            Questions question, QuestionsRequestDto.CreateOneDto questionsRequestDto){
        switch (question.getQuestionsType()){
            case MULTIPLE_CHOICE_ANSWER -> {
                if(questionsRequestDto.getShortAnswer() != null
                        || questionsRequestDto.getMultipleChoiceAnswerList() == null){
                    throw new AnswersCustomException(AnswersExceptionCode.NEED_MULTIPLE_CHOICE_ANSWER);
                }
                List<String> multipleChoiceAnswerList
                        = questionsRequestDto.getMultipleChoiceAnswerList();
                List<Answers> answerList = new ArrayList<>();
                for(String answerText : multipleChoiceAnswerList){
                    Answers answer = createRelations(question, answerText);
                    answerList.add(answer);
                }
                return answerList;
            }
            case SHORT_ANSWER -> {
                if(questionsRequestDto.getMultipleChoiceAnswerList() != null
                        || questionsRequestDto.getShortAnswer() == null){
                    throw new AnswersCustomException(AnswersExceptionCode.NEED_SHORT_ANSWER);
                }
                String answerText = questionsRequestDto.getShortAnswer();
                Answers answer = createRelations(question, answerText);
                List<Answers> answerList = new ArrayList<>();
                answerList.add(answer);
                return answerList;
            }
            default -> {
                return null;
            }
        }
    }

    private Answers createRelations(Questions question, String answerText){
        Answers answer = AnswersConverter.toEntity(question, answerText);
        answersRepository.save(answer);
        question.addAnswer(answer);
        return answer;
    }
}
