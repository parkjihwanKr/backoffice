package com.example.backoffice.domain.event.repository;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.MemberDepartment;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsRepositoryQuery {

    Long countVacationingMembers(LocalDateTime customStartDate);

    List<Events> findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
            EventType eventType, MemberDepartment department, LocalDateTime start, LocalDateTime end);

    List<Events> findAllByMemberIdAndEventTypeAndDateRange(
            Long memberId, EventType eventType,
            LocalDateTime startDate, LocalDateTime endDate);
}
