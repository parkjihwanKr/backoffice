package com.example.backoffice.domain.vacation.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.example.backoffice.domain.vacation.exception.VacationsCustomException;
import com.example.backoffice.domain.vacation.exception.VacationsExceptionCode;
import com.example.backoffice.domain.vacation.entity.VacationPeriodProvider;

import java.time.LocalDateTime;
import java.util.List;

public interface VacationsServiceV1 {

    /**
     * 휴가 하나 저장
     * @param vacation : 저장할 휴가 엔티티
     * @return : 저장된 휴가 하나
     */
    Vacations save(Vacations vacation);


    /**
     * 해당하는 특정달에 대한 휴가 리스트 조회
     * @param monthOfStartDay : 특정 달의 시작 일
     * @param monthOfEndDay : 특정 달의 마지막 일
     * @return 해당하는 특정달에 대한 휴가 리스트
     */
    List<Vacations> findVacationsOnMonth(LocalDateTime monthOfStartDay, LocalDateTime monthOfEndDay);

    /**
     * 특정 필드에 따라 필터링된 휴가 리스트 조회
     * @param memberId : 해당하는 멤버 아이디
     * @param isAccepted : 승인 여부
     * @param startDate : 시작일
     * @param endDate : 마지막 일
     * @return : 해당하는 파라미터들을 필터링한 휴가 리스트
     */
    List<Vacations> findAcceptedVacationByMemberIdAndDateRange(
            Long memberId, Boolean isAccepted,
            LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 해당 멤버의 전달받은 시작일로부터 예정된 휴가까지 리스트를 조회
     * @param memberId : 멤버 아이디
     * @param startDate : 시작일
     * @return : 해당 해당 멤버의 전달받은 시작일로부터 예정된 휴가까지 리스트
     */
    List<Vacations> findAllByMemberIdAndStartDate(Long memberId, LocalDateTime startDate);

    /**
     * 휴가 하나 삭제
     * @param vacationId : 휴가 아이디
     */
    void deleteById(Long vacationId);

    /**
     * 해당 날짜에 휴가 가는 인원 수를 조회
     * @param currentDate : 특정 일의 시간
     * @return 해당 날짜에 휴가 가는 인원 수
     */
    Long countVacationingMembers(LocalDateTime currentDate);

    /**
     * 휴가 하나 조회
     * @param vacationId : 조회할 휴가 아이디
     * @return : 해당하는 휴가 하나
     * @throws VacationsCustomException {@link VacationsExceptionCode#NOT_FOUND_VACATIONS}
     * 해당 휴가를 찾을 수 없는 경우
     */
    Vacations findById(Long vacationId);

    /**
     * 데일리 스케줄러를 통한 휴가 종료일이 어제인 휴가들을 리스트로 조회
     * @param endOfYesterday : 어제의 23시 59분 59초
     * @return : 휴가 종료일이 어제인 휴가들을 리스트
     */
    List<Vacations> findAllBetweenYesterday(LocalDateTime endOfYesterday);

    /**
     * 해당하는 시간에 시작되는 휴가 리스트를 조회
     * @param startOfToday : 오늘 00시 00분 00초
     * @return : 오늘 휴가 리스트
     */
    List<Vacations> findAllByStartDate(LocalDateTime startOfToday);

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
     * 메인 페이지의에서 오늘부터 6일 이후까지의 개인적인 휴가 리스트 조회
     * @param memberId : 해당 멤버 아이디
     * @return : 해당 멤버의 오늘부터 6일 이후까지의 휴가 리스트 응답 DTO
     */
    List<VacationsResponseDto.ReadSummaryOneDto> getPersonalVacationDtoList(
            Long memberId);

    /**
     * 해당하는 휴가 정정 기간이 존재하는지
     * @param key : redis key
     */
    Boolean existPeriod(String key);

    /**
     * 해당하는 휴가 정정 기간을 삭제
     * @param key : redis key
     */
    void deletePeriodByKey(String key);

    /**
     * 휴가 정정 기간 저장
     * @param key : redis key
     * @param minutes : ttl
     * @param values : redis values
     * @param <T> : String type
     */
    <T> void savePeriod(String key, int minutes, T values);

    /**
     * 휴가 정정 기간의 key을 통한 시작일과 마지막 일 찾기
     * @param key {@link VacationPeriodProvider}
     * 해당 링크를 통해 만들어진 키
     * @return 해당하는 키에 대한 value
     */
    String getValueByKey(String key);
}