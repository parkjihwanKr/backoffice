package com.example.backoffice.domain.evaluation.facade;

import com.example.backoffice.domain.evaluation.dto.EvaluationsRequestDto;
import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.member.entity.Members;

public interface EvaluationsServiceFacadeV1 {

    /**
     * 부서 설문조사 한 개 생성
     * @param loginMember : 로그인 멤버
     * @param requestDto {@link EvaluationsRequestDto.CreateOneForDepartmentDto}
     * 부서 설문조사 한 개 생성 요청 DTO
     * @return {@link EvaluationsResponseDto.CreateOneForDepartmentDto}
     * 부서 설문조사 한 개 생성 응답 DTO
     */
    EvaluationsResponseDto.CreateOneForDepartmentDto createOneDepartmentType(
            Members loginMember, EvaluationsRequestDto.CreateOneForDepartmentDto requestDto);

    /**
     * 요청한 년, 분기에 따른 부서 설문조사 한 개 조회
     * @param year : 요청 받는 년도
     * @param quarter : 요청 받는 분기
     * @param evaluationsId : 조회하려는 설문조사 아이디
     * @param loginMember : 로그인 사용자
     * @return {@link EvaluationsResponseDto.ReadOneForDepartmentDto}
     * 부서 설문조사 한 개 조회 응답 DTO
     */
    EvaluationsResponseDto.ReadOneForDepartmentDto readOneDepartmentType(
            Integer year, Integer quarter, Long evaluationsId, Members loginMember);

    /**
     * 부서 설문조사 한 개 수정
     * @param evaluationId : 수정하려는 설문조사 아이디
     * @param loginMember : 로그인 멤버
     * @param requestDto {@link EvaluationsRequestDto.UpdateOneForDepartmentDto}
     * 부서 설문조사 한 개 수정 요청 DTO
     * @return {@link EvaluationsResponseDto.UpdateOneForDepartmentDto}
     * 부서 설문조사 한 개 수정 응답 DTO
     */
    EvaluationsResponseDto.UpdateOneForDepartmentDto updateOneDepartmentType(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.UpdateOneForDepartmentDto requestDto);

    /**
     * 회사 설문조사 한 개 생성
     * @param loginMember : 로그인 멤버
     * @param requestDto {@link EvaluationsRequestDto.CreateOneForCompanyDto}
     * 회사 설문조사 한 개 생성 요청 DTO
     * @return {@link EvaluationsResponseDto.CreateOneForCompanyDto}
     * 회사 설문조사 한 개 생성 응답 DTO
     */
    EvaluationsResponseDto.CreateOneForCompanyDto createOneCompanyType(
            Members loginMember, EvaluationsRequestDto.CreateOneForCompanyDto requestDto);

    /**
     * 요청한 년, 분기에 따른 회사 설문조사 한 개 조회
     * @param year : 요청 받는 년도
     * @param evaluationId : 조회하려는 설문조사 아이디
     * @param loginMember : 로그인 사용자
     * @return {@link EvaluationsResponseDto.ReadOneForCompanyDto}
     * 회사 설문조사 한 개 조회 응답 DTO
     */
    EvaluationsResponseDto.ReadOneForCompanyDto readOneCompanyType(
            Integer year, Long evaluationId, Members loginMember);

    /**
     * 회사 설문조사 한 개 수정
     * @param evaluationId : 수정하려는 설문조사 아이디
     * @param loginMember : 로그인 멤버
     * @param requestDto {@link EvaluationsRequestDto.UpdateOneForCompanyDto}
     * 회사 설문조사 한 개 수정 요청 DTO
     * @return {@link EvaluationsResponseDto.UpdateOneForCompanyDto}
     * 회사 설문조사 한 개 수정 응답 DTO
     */
    EvaluationsResponseDto.UpdateOneForCompanyDto updateOneCompanyType(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.UpdateOneForCompanyDto requestDto);

    /**
     * 설문조사 한 개 삭제
     * @param evaluationId : 삭제하려는 설문조사 아이디
     * @param loginMember : 로그인 멤버
     */
    void deleteOne(Long evaluationId, Members loginMember);

    /**
     * 설문조사 한 개 제출
     * @param evaluationId : 제출하려는 설문조사 아이디
     * @param loginMember : 로그인 멤버
     * @param requestDto : {@link EvaluationsRequestDto.SubmitOneDto}
     * 설문조사 한 개 제출 요청 DTO
     * @return {@link EvaluationsResponseDto.SubmitOneDto}
     * 설문조사 한 개 제출 응답 DTO
     */
    EvaluationsResponseDto.SubmitOneDto submitOne(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.SubmitOneDto requestDto);

    /**
     * 제출한 설문조사 제출 취소
     * @param evaluationId : 제출을 취소하려는 설문조사 아이디
     * @param loginMember : 로그인 멤버
     */
    void deleteSubmittedOneForCancellation(Long evaluationId, Members loginMember);
}
