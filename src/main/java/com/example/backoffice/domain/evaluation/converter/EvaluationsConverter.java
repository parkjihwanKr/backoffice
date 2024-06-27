package com.example.backoffice.domain.evaluation.converter;

import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public class EvaluationsConverter {

    public static Evaluations toEntity(
            String title, Integer year, Integer quarter,
            String description, MemberDepartment department, List<Members> memberList){
        return Evaluations.builder()
                .title(title)
                .year(year)
                .quarter(quarter)
                .description(description)
                .department(department)
                .memberList(memberList)
                .build();
    }

    public static EvaluationsResponseDto.CreateOneForDepartmentDto toCreateOneForDepartmentDto(
            String loginMemberName, String title, String description){
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
            String title, Integer year, Integer quarter, String writerName){
        return EvaluationsResponseDto.ReadOneForDepartmentDto.builder()
                .title(title)
                .year(year)
                .quarter(quarter)
                .writerName(writerName)
                .build();
    }
}
