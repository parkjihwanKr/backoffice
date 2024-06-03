package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.converter.EventsConverter;
import com.example.backoffice.domain.event.dto.EventDateRangeDto;
import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.event.exception.EventsExceptionCode;
import com.example.backoffice.domain.event.repository.EventsRepository;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService{

    private final MembersService membersService;
    private final EventsRepository eventsRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    @Transactional
    public EventsResponseDto.CreateCompanyEventResponseDto createCompanyEvent(
            Members loginMember, EventsRequestDto.CreateCompanyEventsRequestDto requestDto){
        // 1. 멤버가 존재하는지?
        membersService.findById(loginMember.getId());

        // 2. 요청 받는 날짜가 다음달로부터 시작되는 날인지?
        EventDateRangeDto eventDateRangeDto
                = validateEventDate(requestDto.getStartDate(), requestDto.getEndDate());

        Events event = EventsConverter.toEntity(requestDto, eventDateRangeDto, loginMember);
        eventsRepository.save(event);
        return EventsConverter.toCreateCompanyDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Events findById(Long eventId){
        return eventsRepository.findById(eventId).orElseThrow(
                ()-> new EventsCustomException(EventsExceptionCode.NOT_FOUND_EVENT)
        );
    }

    public EventDateRangeDto validateEventDate(String startDate, String endDate){
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startEventDate = LocalDateTime.parse(startDate, DATE_TIME_FORMATTER);
        LocalDateTime endEventDate = LocalDateTime.parse(endDate, DATE_TIME_FORMATTER);

        // 1. 시작날이 다음 달로 시작하는지?
        if(!startEventDate.getMonth().equals(now.plusMonths(1).getMonth())){
            throw new EventsCustomException(EventsExceptionCode.INVALID_START_DATE);
        }

        // 2. 시작날이 마지막 날보다 더 빠른지?
        if(!startEventDate.isAfter(endEventDate)){
            throw new EventsCustomException(EventsExceptionCode.END_DATE_BEFORE_START_DATE);
        }

        return EventsConverter.toEventDateRangeDto(startEventDate, endEventDate);
    }
}
