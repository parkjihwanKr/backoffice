package com.example.backoffice.domain.evaluation.service;

import com.example.backoffice.domain.evaluation.dto.EvaluationsRequestDto;
import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;

public interface EvaluationsServiceV1 {

    EvaluationsResponseDto.CreateOneForDepartmentDto createOneForDepartment(
            Members loginMember, EvaluationsRequestDto.CreateOneForDepartmentDto requestDto);

    EvaluationsResponseDto.CreateOneForCompanyDto createOneForCompany(
            Members loginMember, EvaluationsRequestDto.CreateOneForCompanyDto requestDto);

    EvaluationsResponseDto.ReadOneForDepartmentDto readOneForDepartment(
            Integer year, Integer quarter, Long evaluationsId, Members member);

    EvaluationsResponseDto.ReadOneForCompanyDto readOneForCompany(
            Integer year, Long evaluationId, Members loginMember);

    EvaluationsResponseDto.UpdateOneForDepartmentDto updateOneForDepartment(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.UpdateOneForDepartmentDto requestDto);

    EvaluationsResponseDto.UpdateOneForCompanyDto updateOneForCompany(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.UpdateOneForCompanyDto requestDto);

    void deleteOne(Long evaluationId, Members loginMember);

    Evaluations findById(Long evaluationsId);

}
