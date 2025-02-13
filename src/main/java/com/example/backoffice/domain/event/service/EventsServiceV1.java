package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.event.exception.EventsExceptionCode;
import com.example.backoffice.domain.member.entity.MemberDepartment;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsServiceV1 {

    /**
     * 새로운 일정 저장
     * @param event 저장하려는  엔티티
     * @return 저장된 일정 엔티티
     */
    Events save(Events event);

    /**
     * 일정 하나를 조회
     * @param eventId 조회하려는 일정 아이디
     * @return 조회된 일정 엔티티
     * @throws EventsCustomException {@link EventsExceptionCode#NOT_FOUND_EVENT}
     * 일정를 찾을 수 없는 경우
     */
    Events findById(Long eventId);

    /**
     * 일정 하나 삭제
     * @param eventId 삭제하려는 일정의 아이디
     */
    void deleteById(Long eventId);

    /**
     * 일정 타입, 부서, 그리고 시작일 또는 종료일이 주어진 기간과 겹치는 모든 일정를 조회
     * @param eventType 조회하려는 일정 타입
     * @param memberDepartment 조회하려는 부서
     * @param start 기간의 시작 날짜와 시간
     * @param end 기간의 종료 날짜와 시간
     * @return 조건에 맞는 일정 리스트
     */
    List<Events> findAllByEventTypeAndDepartmentAndStartOrEndDateBetween(
            EventType eventType, MemberDepartment memberDepartment,
            LocalDateTime start, LocalDateTime end);

    /**
     * 일정 타입, 부서, 그리고 시작일 또는 종료일이 주어진 기간과 겹치는 모든 일정를 조회
     * @param eventType 조회하려는 일정 타입
     * @param department 조회하려는 부서
     * @param start 기간의 시작 날짜와 시간
     * @param end 기간의 종료 날짜와 시간
     * @return 조건에 맞는 일정 리스트
     */
    List<Events> findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
            EventType eventType, MemberDepartment department,
            LocalDateTime start, LocalDateTime end);

    /**
     * 요약된 부서 일정 조회
     * @param department : 조회하려는 부서
     * @return 메인 페이지에서 사용할 요약된 부서 일정 응답 DTO
     */
    List<EventsResponseDto.ReadCompanySummaryOneDto> getCompanyEventDtoList(
            MemberDepartment department);
}
