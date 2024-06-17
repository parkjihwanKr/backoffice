package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.event.exception.EventsExceptionCode;
import com.example.backoffice.domain.event.repository.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsServiceImplV1 implements EventsService {

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
    @Transactional(readOnly = true)
    public List<Events> findAllByEventTypeAndEndDateBefore(
            EventType eventType, LocalDateTime endOfDay) {
        return eventsRepository.findAllByEventTypeAndEndDateBefore(eventType, endOfDay);
    }

    @Override
    @Transactional
    public Long countVacationingMembers(LocalDateTime customStartDate) {
        return eventsRepository.countVacationingMembers(customStartDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Events> findAllByEventTypeAndStartDateBetween(
            EventType eventType, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return eventsRepository.findAllByEventTypeAndStartDateBetween(eventType, startOfDay, endOfDay);
    }
}
