package com.example.backoffice.domain.question.converter;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.entity.Questions;
import com.example.backoffice.domain.question.entity.QuestionsType;
import com.example.backoffice.domain.question.exception.QuestionsCustomException;
import com.example.backoffice.domain.question.exception.QuestionsExceptionCode;

import java.util.List;

public class QuestionsConverter {

    public static Questions toEntity(
            Evaluations evaluation, QuestionsType questionsType,
            String questionText, Long order){
        return Questions.builder()
                .evaluation(evaluation)
                .questionsType(questionsType)
                .questionText(questionText)
                .order(order)
                .build();
    }

    public static QuestionsType toQuestionsType(String questionsType){
        return switch (questionsType) {
            case "SHORT_ANSWER" -> QuestionsType.SHORT_ANSWER;
            case "MULTIPLE_CHOICE_ANSWER" -> QuestionsType.MULTIPLE_CHOICE_ANSWER;
            default -> throw new QuestionsCustomException(QuestionsExceptionCode.NOT_FOUND_QUESTION_TYPE);
        };
    }

    public static QuestionsResponseDto.CreateOneDto toCreateOneDto(
            String questionText, QuestionsType questionsType, Integer questionsNumber
            /*List<String> multipleChoiceAnswerList*/){
        return QuestionsResponseDto.CreateOneDto.builder()
                .questionNumber(questionsNumber)
                .questionText(questionText)
                .questionType(questionsType)
                /*.multipleChoiceAnswerList(multipleChoiceAnswerList)*/
                .build();
    }

    public static QuestionsResponseDto.CreateAllDto toCreateAllDto(
            String evaluationTitle, Integer evaluationYear, Integer evaluationQuarter,
            List<QuestionsResponseDto.CreateOneDto> questionList){
        return QuestionsResponseDto.CreateAllDto.builder()
                .evaluationTitle(evaluationTitle)
                .year(evaluationYear)
                .quarter(evaluationQuarter)
                .questionList(questionList)
                .build();
    }

    public static QuestionsResponseDto.UpdateOneDto toUpdateOneDto(Questions question){
        return QuestionsResponseDto.UpdateOneDto.builder()
                .questionNumber(question.getOrder())
                .questionText(question.getQuestionText())
                .questionsType(question.getQuestionsType())
                .multipleChoiceAnswerList(question.getMultipleChoiceAnswerList())
                .build();
    }

    public static QuestionsResponseDto.UpdateOneForOrderDto toUpdateOneForChangedOrderDto(
            Long beforeOrder, Long updateOrder, String questionText,
            QuestionsType questionsType, List<Answers> multipleChoiceAnswerList){
        return QuestionsResponseDto.UpdateOneForOrderDto.builder()
                .beforeUpdatedOrder(beforeOrder)
                .updatedOrder(updateOrder)
                .questionText(questionText)
                .questionsType(questionsType)
                .multipleChoiceAnswerList(multipleChoiceAnswerList)
                .build();
    }
}
