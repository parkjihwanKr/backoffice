package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.MemberDepartment;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsServiceV1 {

    Events save(Events event);

    Events findById(Long eventId);

    void deleteById(Long eventId);

    List<Events> findAllByStartDateBetween(LocalDateTime start, LocalDateTime end);

    List<Events> findAllByEventTypeAndDepartmentAndStartOrEndDateBetween(
            EventType eventType, MemberDepartment memberDepartment,
            LocalDateTime start, LocalDateTime end);

    List<Events> findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
            EventType eventType, MemberDepartment department,
            LocalDateTime start, LocalDateTime end);
}
