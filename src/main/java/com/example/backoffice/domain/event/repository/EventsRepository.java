package com.example.backoffice.domain.event.repository;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<Events, Long>, EventsRepositoryQuery {

    List<Events> findAllByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Events> findAllByEventTypeAndStartDateBetween(
            EventType eventType, LocalDateTime startDate, LocalDateTime endDate);

    List<Events> findAllByEventTypeAndEndDateBefore(
            EventType eventType, LocalDateTime endDate);
}
