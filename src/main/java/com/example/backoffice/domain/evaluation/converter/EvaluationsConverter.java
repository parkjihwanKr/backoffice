package com.example.backoffice.domain.evaluation.converter;

import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.question.entity.Questions;

import java.util.List;

public class EvaluationsConverter {

    public static Evaluations toEntity(
            String title, Integer year, Integer quarter,
            String description, MemberDepartment department){
        return Evaluations.builder()
                .title(title)
                .year(year)
                .quarter(quarter)
                .description(description)
                .department(department)
                .build();
    }

    public static EvaluationsResponseDto.CreateOneForDepartmentDto toCreateOneForDepartmentDto(
            String title, String description, String loginMemberName){
        return EvaluationsResponseDto.CreateOneForDepartmentDto.builder()
                .writerName(loginMemberName)
                .title(title)
                .description(description)
                .build();
    }

    public static EvaluationsResponseDto.CreateOneForCompanyDto toCreateOneForCompanyDto(
            String loginMemberName, String title, String description){
        return EvaluationsResponseDto.CreateOneForCompanyDto.builder()
                .writerName(loginMemberName)
                .title(title)
                .description(description)
                .build();
    }

    public static EvaluationsResponseDto.ReadOneForDepartmentDto toReadOneForDepartmentDto(
            String title, Integer year, Integer quarter, String writerName,
            List<Questions> questionList){
        return EvaluationsResponseDto.ReadOneForDepartmentDto.builder()
                .title(title)
                .year(year)
                .quarter(quarter)
                .writerName(writerName)
                .evaluationQuestionList(questionList)
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
}
