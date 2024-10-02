package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {

    Events save(Events event);

    Events findById(Long eventId);

    void deleteById(Long eventId);

    List<Events> findAllByStartDateBetween(LocalDateTime start, LocalDateTime end);

/*    List<Events> findAllByEventTypeAndEndDateBefore(EventType eventType, LocalDateTime endOfDay);

    List<Events> findAllByEventTypeAndStartDateBetween(
            EventType eventType, LocalDateTime startOfDay, LocalDateTime endOfDay);*/

    List<Events> findAllByEventTypeAndDepartmentAndStartOrEndDateBetween(
            EventType eventType, MemberDepartment memberDepartment,
            LocalDateTime start, LocalDateTime end);

    List<Events> findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
            EventType eventType, MemberDepartment department,
            LocalDateTime start, LocalDateTime end);

    List<Events> findAllByMemberIdAndEventTypeAndDateRange(
            Long memberId, EventType eventType,
            LocalDateTime startDate, LocalDateTime endDate);
}
