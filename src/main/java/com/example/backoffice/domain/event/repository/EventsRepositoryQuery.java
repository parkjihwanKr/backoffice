package com.example.backoffice.domain.event.repository;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.MemberDepartment;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsRepositoryQuery {
    List<Events> findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
            EventType eventType, MemberDepartment department, LocalDateTime start, LocalDateTime end);
}
