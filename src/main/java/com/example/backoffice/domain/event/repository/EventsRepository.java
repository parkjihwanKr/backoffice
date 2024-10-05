package com.example.backoffice.domain.event.repository;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long>, EventsRepositoryQuery {

    List<Events> findAllByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
