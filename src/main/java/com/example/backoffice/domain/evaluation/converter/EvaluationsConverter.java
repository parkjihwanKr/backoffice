package com.example.backoffice.domain.evaluation.converter;

import com.example.backoffice.domain.answer.entity.Answers;
import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.question.dto.QuestionsResponseDto;
import com.example.backoffice.domain.question.entity.Questions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EvaluationsConverter {

    public static Evaluations toEntity(
            String title, Integer year, Integer quarter,
            String description, MemberDepartment department,
            LocalDate startDate, LocalDate endDate){
        return Evaluations.builder()
                .title(title)
                .year(year)
                .quarter(quarter)
                .description(description)
                .department(department)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static EvaluationsResponseDto.CreateOneForDepartmentDto toCreateOneForDepartmentDto(
            String title, String description, String loginMemberName,
            LocalDate startDate, LocalDate endDate){
        return EvaluationsResponseDto.CreateOneForDepartmentDto.builder()
                .writerName(loginMemberName)
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static EvaluationsResponseDto.CreateOneForCompanyDto toCreateOneForCompanyDto(
            String loginMemberName, String title, String description,
            LocalDate startDate, LocalDate endDate){
        return EvaluationsResponseDto.CreateOneForCompanyDto.builder()
                .writerName(loginMemberName)
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static EvaluationsResponseDto.ReadOneForDepartmentDto toReadOneForDepartmentDto(
            String title, Integer year, Integer quarter, String writerName,
            List<Questions> questionList){
        List<QuestionsResponseDto.ReadOneDto> questionResponseDtoList = new ArrayList<>();
        for(Questions question : questionList){
            List<String> answerList = new ArrayList<>();
            for(Answers answer : question.getMultipleChoiceAnswerList()){
                answerList.add(answer.getText());
            }
            questionResponseDtoList.add(
                    QuestionsResponseDto.ReadOneDto.builder()
                            .questionNumber(question.getNumber())
                            .questionText(question.getQuestionText())
                            .questionsType(question.getQuestionsType())
                            .multipleAnswerList(answerList)
                            .build()
            );
        }

        return EvaluationsResponseDto.ReadOneForDepartmentDto.builder()
                .title(title)
                .year(year)
                .quarter(quarter)
                .writerName(writerName)
                .questionList(questionResponseDtoList)
                .build();
    }

    public static EvaluationsResponseDto.ReadOneForCompanyDto toReadOneForCompanyDto(
            String title, String description, Integer year){
        return EvaluationsResponseDto.ReadOneForCompanyDto.builder()
                .title(title)
                .description(description)
                .year(year)
                .build();
    }

    public static EvaluationsResponseDto.UpdateOneForDepartmentDto toUpdateOneForDepartmentDto(
            MemberDepartment department, String title, String description,
            Integer year, Integer quarter, String writerName,
            LocalDate startDate, LocalDate endDate){
        return EvaluationsResponseDto.UpdateOneForDepartmentDto.builder()
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
}
