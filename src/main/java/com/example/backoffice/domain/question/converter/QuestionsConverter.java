package com.example.backoffice.domain.question.converter;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.entity.Questions;
import com.example.backoffice.domain.question.entity.QuestionsType;
import com.example.backoffice.domain.question.exception.QuestionsCustomException;
import com.example.backoffice.domain.question.exception.QuestionsExceptionCode;

import java.util.ArrayList;
import java.util.List;

public class QuestionsConverter {

    public static Questions toEntity(
            Evaluations evaluation, QuestionsType questionsType,
            String questionText, Long number){
        return Questions.builder()
                .evaluation(evaluation)
                .questionsType(questionsType)
                .questionText(questionText)
                .number(number)
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
            Long questionId, String questionText, QuestionsType questionsType, Integer questionsNumber,
            List<Answers> multipleChoiceAnswerList){
        List<String> changeAnswerList = new ArrayList<>();
        for(Answers answer : multipleChoiceAnswerList){
            changeAnswerList.add(answer.getText());
        }
        return QuestionsResponseDto.CreateOneDto.builder()
                .questionId(questionId)
                .questionNumber(questionsNumber)
                .questionText(questionText)
                .questionType(questionsType)
                .multipleChoiceAnswerList(changeAnswerList)
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
                .questionId(question.getId())
                .questionNumber(question.getNumber())
                .questionText(question.getQuestionText())
                .questionsType(question.getQuestionsType())
                .multipleChoiceAnswerList(question.getMultipleChoiceAnswerList())
                .build();
    }

    public static QuestionsResponseDto.UpdateOneForOrderDto toUpdateOneForChangedOrderDto(
            Long questionId, Long previousNumber, Long updatedNumber, String questionText,
            QuestionsType questionsType, List<Answers> multipleChoiceAnswerList){
        return QuestionsResponseDto.UpdateOneForOrderDto.builder()
                .questionId(questionId)
                .previousNumber(previousNumber)
                .updatedNumber(updatedNumber)
                .questionText(questionText)
                .questionsType(questionsType)
                .multipleChoiceAnswerList(multipleChoiceAnswerList)
                .build();
    }

    public static Questions toUpdateOneForChangedOrder(Questions question, Long changedNumber){
        return Questions.builder()
                .questionText(question.getQuestionText())
                .number(changedNumber)
                .multipleChoiceAnswerList(question.getMultipleChoiceAnswerList())
                .evaluation(question.getEvaluation())
                .questionsType(question.getQuestionsType())
                .build();
    }
}
