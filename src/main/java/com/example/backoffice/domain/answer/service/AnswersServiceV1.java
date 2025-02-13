package com.example.backoffice.domain.answer.service;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.entity.Questions;

import java.util.List;

public interface AnswersServiceV1 {

    List<Answers> createAll(
            Questions question, QuestionsRequestDto.CreateOneDto questionsRequestDto);

    List<Answers> findAllByQuestionId(Long questionId);

    List<Answers> updateAllForMultipleChoiceAnswer(
            Questions question, List<String> changeQuestionTextList);

    void updateAllForShortAnswer(Long questionId);

    Answers findByQuestionIdAndNumber(Long questionId, Long number);

    Answers findByQuestionId(Long questionId);
}
