package com.example.backoffice.domain.vacation.repository;


import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.entity.Vacations;

import java.time.LocalDateTime;
import java.util.List;

public interface VacationsQuery {
    /**
     * 해당 날짜에 휴가를 나간 멤버 수 조회
     * @param customDate : 해당 날
     * @return :해당 날짜에 휴가를 나간 멤버 수
     */
    long countVacationingMembers(LocalDateTime customDate);

    /**
     * 해당하는 달에 휴가를 나타는 휴가 리스트 조회
     * @param startDayOfMonth : 해당 달의 시작일
     * @param endDayOfMonth : 해당 달의 마지막 일
     * @return : 해당하는 달에 휴가를 나타는 휴가 리스트
     */
    List<Vacations> findVacationsOnMonth(
            LocalDateTime startDayOfMonth, LocalDateTime endDayOfMonth);

    /**
     * 데일리 스케줄러를 통해 휴가 마감일이 어제였던 휴가 리스트 조회
     * @param endOfYesterday : 어제의 23시 59분 59초
     * @return : 휴가 마감일이 어제였던 휴가 리스트
     */
    List<Vacations> findAllBetweenYesterday(LocalDateTime endOfYesterday);

    /**
     * 오늘이 휴가 시작일인 휴가 리스트 조회
     * @param now : 오늘 00시 00분 00초
     * @return : 오늘이 휴가 시작일인 휴가 리스트
     */
    List<Vacations> findAllByStartDate(LocalDateTime now);

    /**
     * 해당하는 파라미터를 필터링하는 휴가 리스트 조회
     * @param memberId : 멤버 아이디
     * @param isAccepted : 승인 여부
     * @param startDate : 시작일
     * @param endDate : 마지막 일
     * @return 해당하는 파라미터를 필터링하는 휴가 리스트
     */
    List<Vacations> findAcceptedVacationByMemberIdAndDateRange(
            Long memberId, Boolean isAccepted,
            LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 해당하는 파라미터를 필터링하는 휴가 리스트 조회
     * @param memberId : 해당하는 멤버 아이디
     * @param startDate : 시작일
     * @return : 해당하는 파라미터를 필터링하는 휴가 리스트
     */
    List<Vacations> findAllByMemberIdAndStartDate(
            Long memberId, LocalDateTime startDate);

    /**
     * 해당 범위 내에 존재하는 멤버의 휴가 참/거짓 판별
     * @param vacationId : 해당하는 휴가 아이디
     * @param memberId : 해당하는 멤버 아이디
     * @param startDate : 해당하는 시작일
     * @param endDate : 해당하는 마지막일
     * @return : 해당 범위 내에 존재하는 멤버의 휴가 참/거짓 여부
     */
    Boolean existsVacationForMemberInDateRange(
            Long vacationId, Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 해당 파라미터들에 따른 필터링된 휴가 리스트 조회
     * @param startDate : 시작일
     * @param endDate : 마지막일
     * @param isAccepted : 승인 여부
     * @param urgent : 긴급 여부
     * @param memberDepartment : 멤버의 부서
     * @return : 해당 파라미터들에 따른 필터링된 휴가 리스트
     */
    List<Vacations> findFilteredVacationsOnMonth(
            LocalDateTime startDate, LocalDateTime endDate,
            Boolean isAccepted, Boolean urgent, MemberDepartment memberDepartment);

    /**
     * 생성일자를 내림차순하여 시작일과 마지막일을 필터링한 멤버의 휴가 리스트 조회
     * @param memberId : 멤버 아이디
     * @param startDate : 시작일
     * @param endDate : 마지막 일
     * @return : 생성일자를 내림차순하여 시작일과 마지막일을 필터링한 멤버의 휴가 리스트
     */
    List<VacationsResponseDto.ReadSummaryOneDto> findFilteredVacations(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate);
}
