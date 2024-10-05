package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.event.exception.EventsExceptionCode;
import com.example.backoffice.domain.event.repository.EventsRepository;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsServiceImplV1 implements EventsServiceV1 {

    private final EventsRepository eventsRepository;

    @Override
    @Transactional
    public Events save(Events event) {
        return eventsRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Events findById(Long eventId) {
        return eventsRepository.findById(eventId).orElseThrow(
                () -> new EventsCustomException(EventsExceptionCode.NOT_FOUND_EVENT)
        );
    }

    @Override
    @Transactional
    public void deleteById(Long eventId) {
        eventsRepository.deleteById(eventId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Events> findAllByStartDateBetween(LocalDateTime start, LocalDateTime end) {
        return eventsRepository.findAllByStartDateBetween(start, end);
    }

    @Override
    @Transactional
    public List<Events> findAllByEventTypeAndDepartmentAndStartOrEndDateBetween(
            EventType eventType, MemberDepartment memberDepartment,
            LocalDateTime start, LocalDateTime end){
        return eventsRepository.findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
                eventType, memberDepartment, start,end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Events> findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
            EventType eventType, MemberDepartment department, LocalDateTime start, LocalDateTime end) {
        // 이벤트 타입과 부서별로 시작 또는 종료일이 주어진 범위와 겹치는 이벤트를 모두 조회
        return eventsRepository.findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
                eventType, department, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Events> findAllByMemberIdAndEventTypeAndDateRange(
            Long memberId, EventType eventType,
            LocalDateTime startDate, LocalDateTime endDate){
        return eventsRepository.findAllByMemberIdAndEventTypeAndDateRange(
                memberId, eventType, startDate, endDate);
    }
}
