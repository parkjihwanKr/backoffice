package com.example.backoffice.domain.evaluation.converter;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.entity.EvaluationType;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.exception.EvaluationsCustomException;
import com.example.backoffice.domain.evaluation.exception.EvaluationsExceptionCode;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.entity.Questions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EvaluationsConverter {

    public static Evaluations toEntity(
            String title, Integer year, Integer quarter,
            String description, MemberDepartment department,
            LocalDate startDate, LocalDate endDate, EvaluationType evaluationType){
        return Evaluations.builder()
                .title(title)
                .year(year)
                .quarter(quarter)
                .description(description)
                .department(department)
                .startDate(startDate)
                .endDate(endDate)
                .evaluationType(evaluationType)
                .build();
    }

    public static EvaluationsResponseDto.CreateOneDto toCreateOneDto(
            Long evaluationId, String title, String description, String loginMemberName,
            LocalDate startDate, LocalDate endDate, EvaluationType evaluationType,
            MemberDepartment department){
        return EvaluationsResponseDto.CreateOneDto.builder()
                .evaluationId(evaluationId)
                .writerName(loginMemberName)
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .evaluationType(evaluationType)
                .memberDepartment(department)
                .build();
    }

    public static EvaluationsResponseDto.ReadOneDto toReadOneDto(
            Long evaluationId, String title, String description, Integer year,
            Integer quarter, String writerName, List<Questions> questionList,
            EvaluationType evaluationType){
        List<QuestionsResponseDto.ReadOneDto> questionResponseDtoList = new ArrayList<>();
        for(Questions question : questionList){
            List<String> answerList = new ArrayList<>();
            for(Answers answer : question.getMultipleChoiceAnswerList()){
                answerList.add(answer.getText());
            }
            questionResponseDtoList.add(
                    QuestionsResponseDto.ReadOneDto.builder()
                            .questionId(question.getId())
                            .questionNumber(question.getNumber())
                            .questionText(question.getQuestionText())
                            .questionsType(question.getQuestionsType())
                            .multipleAnswerList(answerList)
                            .build()
            );
        }

        return EvaluationsResponseDto.ReadOneDto.builder()
                .evaluationId(evaluationId)
                .title(title)
                .description(description)
                .year(year)
                .quarter(quarter)
                .writerName(writerName)
                .evaluationType(evaluationType)
                .questionList(questionResponseDtoList)
                .build();
    }

    public static EvaluationsResponseDto.UpdateOneDto toUpdateOneDto(
            Long evaluationId, MemberDepartment department, String title, String description,
            Integer year, Integer quarter, String writerName,
            LocalDate startDate, LocalDate endDate){
        return EvaluationsResponseDto.UpdateOneDto.builder()
                .evaluationId(evaluationId)
                .department(department)
                .title(title)
                .description(description)
                .year(year)
                .quarter(quarter)
                .writerName(writerName)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static EvaluationsResponseDto.SubmitOneDto toSubmitOneDto(
            Long evaluationId, String submitterName){
        return EvaluationsResponseDto.SubmitOneDto.builder()
                .evaluationId(evaluationId)
                .createdAt(LocalDateTime.now())
                .submitterName(submitterName)
                .build();
    }

    public static EvaluationType toEvaluationType(String evaluationType){
        for (EvaluationType type : EvaluationType.values()) {
            if (type.getLabel().equalsIgnoreCase(evaluationType)) {
                return type;
            }
        }
        throw new EvaluationsCustomException(EvaluationsExceptionCode.NOT_FOUND_EVALUATION_TYPE);
    }
}
