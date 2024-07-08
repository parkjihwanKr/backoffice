package com.example.backoffice.domain.answer.service;

import com.example.backoffice.domain.answer.converter.AnswersConverter;
import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.answer.exception.AnswersCustomException;
import com.example.backoffice.domain.answer.exception.AnswersExceptionCode;
import com.example.backoffice.domain.answer.repository.AnswersRepository;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.entity.Questions;
import com.example.backoffice.domain.question.entity.QuestionsType;
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
                if(questionsRequestDto.getMultipleChoiceAnswerList() == null){
                    throw new AnswersCustomException(AnswersExceptionCode.NEED_MULTIPLE_CHOICE_ANSWER);
                }
                List<String> multipleChoiceAnswerList
                        = questionsRequestDto.getMultipleChoiceAnswerList();
                List<Answers> answerList = new ArrayList<>();
                long i = 1L;
                for(String answerText : multipleChoiceAnswerList){
                    Answers answer = createRelations(question, answerText, i);
                    answerList.add(answer);
                    i++;
                }
                return answerList;
            }
            case SHORT_ANSWER -> {
                if(questionsRequestDto.getMultipleChoiceAnswerList() != null){
                    throw new AnswersCustomException(AnswersExceptionCode.NEED_SHORT_ANSWER);
                }
                Answers answer = createRelations(question, null, null);
                List<Answers> answerList = new ArrayList<>();
                answerList.add(answer);
                return answerList;
            }
            default -> throw new AnswersCustomException(AnswersExceptionCode.NOT_FOUND_QUESTIONS_TYPE);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Answers> findAllByQuestionId(Long questionId){
        return answersRepository.findAllByQuestionId(questionId);
    }

    @Override
    @Transactional
    public List<Answers> updateAllForMultipleChoiceAnswer(Questions question, List<String> changeQuestionTextList){
        List<Answers> existingAnswerList = findAllByQuestionId(question.getId());
        if(existingAnswerList.size() < 2){
            throw new AnswersCustomException(
                    AnswersExceptionCode.NOT_FOUND_ANSWER_LIST_BY_QUESTION_ID_OR_NEED_CHOICE_SHORT_ANSWER);
        }

        List<Answers> updateAnswerList = new ArrayList<>();

        // 1. 기존 리스트가 변경되는 질문 리스트보다 적을 때
        int existingAnswerListSize = existingAnswerList.size();
        int changingAnswerListSize = changeQuestionTextList.size();
        // 5 > 3
        if(existingAnswerListSize >= changingAnswerListSize){
            int i = 0;
            for(Answers answer : existingAnswerList){
                if(changeQuestionTextList.get(i) != null){
                    answer.update(changeQuestionTextList.get(i));
                    i++;
                }else{
                    answersRepository.deleteByText(
                            existingAnswerList.get(i).getText());
                }
                updateAnswerList.add(answer);
            }
        // 2. 기존 리스트가 변경되는 질문 리스트보다 많을 때
        }else{
            int i = 0;
            for(Answers answer : existingAnswerList){
                answer.update(changeQuestionTextList.get(i));
                updateAnswerList.add(answer);
                i++;
            }
            for(int j = i; j<changingAnswerListSize; j++){
                Answers answer = AnswersConverter.toEntity(question, changeQuestionTextList.get(j), (long)j);
                answersRepository.save(answer);
                updateAnswerList.add(answer);
            }
        }
        return updateAnswerList;
    }

    @Override
    @Transactional
    public void updateAllForShortAnswer(Long questionId){
        List<Answers> existingAnswerList = findAllByQuestionId(questionId);
        if(existingAnswerList.isEmpty()){
            throw new AnswersCustomException(AnswersExceptionCode.NOT_FOUND_ANSWER_LIST_BY_QUESTION_ID);
        }
        answersRepository.deleteAll(existingAnswerList);
    }

    private Answers createRelations(Questions question, String answerText, Long number){
        // 질문 1. 회사 생활에 만족하십니까? -> 1. 매우 만족
        // 질문 1. 회사 생활에 만족하십니까? -> 2. 보통
        // 질문 2. 회사 생활에서 불편하신 부분을 서술해주세요. -> answerText null
        Answers answer;
        switch (question.getQuestionsType()) {
            case SHORT_ANSWER, MULTIPLE_CHOICE_ANSWER
                    -> answer = AnswersConverter.toEntity(question, answerText, number);
            default
                    -> throw new AnswersCustomException(
                            AnswersExceptionCode.NOT_FOUND_QUESTIONS_TYPE);
        }
        answersRepository.save(answer);
        // question.addAnswer(answer);
        return answer;
    }
}
