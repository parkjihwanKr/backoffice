package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {

    Events save(Events event);

    Events findById(Long eventId);

    void deleteById(Long eventId);

    List<Events> findAllByStartDateBetween(LocalDateTime start, LocalDateTime end);

    List<Events> findAllByEventTypeAndEndDateBefore(EventType eventType, LocalDateTime endOfDay);

    Long countVacationingMembers(LocalDateTime customStartDate);

    List<Events> findAllByEventTypeAndStartDateBetween(
            EventType eventType, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
