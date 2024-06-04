package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.converter.EventsConverter;
import com.example.backoffice.domain.event.dto.EventDateRangeDto;
import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.EventCrudType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.event.exception.EventsExceptionCode;
import com.example.backoffice.domain.event.repository.EventsRepository;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.notification.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsServiceImplV1 implements EventsService{

    private final MembersService membersService;
    private final NotificationsService notificationsService;
    private final EventsRepository eventsRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    @Transactional
    public EventsResponseDto.CreateDepartmentEventResponseDto createDepartmentEvent(
            Members loginMember, EventsRequestDto.CreateDepartmentEventsRequestDto requestDto){
        // 요청 받는 날짜가 다음달로부터 시작되는 날인지?
        validateMemberDepartment(loginMember, requestDto.getDepartment());

        EventDateRangeDto eventDateRangeDto
                = validateEventDate(requestDto.getStartDate(),
                requestDto.getEndDate(), EventCrudType.CREATE);

        Events event = EventsConverter.toEntity(requestDto, eventDateRangeDto, loginMember);
        eventsRepository.save(event);
        return EventsConverter.toCreateDepartmentDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventsResponseDto.ReadCompanyMonthEventResponseDto> readCompanyMonthEvent(
            Long year, Long month){
        // 해당 날짜의 정보를 가지고 오는데, 만약 2024-06-23~07-02까지의 프로젝트라면?
        // 해당 이벤트를 가지고 오는지? 아닌지 확인해야함
        LocalDateTime start = YearMonth.of(
                year.intValue(), month.intValue()).atDay(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1).minusNanos(1);
        List<Events> eventList = eventsRepository.findAllByStartDateBetween(start, end);

        return EventsConverter.toReadCompanyMonthDto(eventList);
    }

    @Override
    @Transactional(readOnly = true)
    public EventsResponseDto.ReadCompanyEventResponseDto readCompanyEvent(
            Long eventId){
        Events event = findById(eventId);
        return EventsConverter.toReadCompanyDto(event);
    }

    @Override
    @Transactional
    public EventsResponseDto.UpdateDepartmentEventResponseDto updateDepartmentEvent(
            Long eventId, Members loginMember,
            EventsRequestDto.UpdateDepartmentEventRequestDto requestDto){
        Events event = findById(eventId);

        // 로그인 멤버의 부서와 요청한 이벤트를 수행할 부서가 같은지?
        // 최고 경영자는 부서에 영향받지 않고 생성 가능
        validateMemberDepartment(loginMember, requestDto.getDepartment());

        EventDateRangeDto eventDateRangeDto
                = validateEventDate(requestDto.getStartDate(),
                requestDto.getEndDate(), EventCrudType.UPDATE);

        event.update(
                requestDto.getTitle(), requestDto.getDescription(),
                requestDto.getDepartment(), eventDateRangeDto.getStartDate(),
                eventDateRangeDto.getEndDate());

        return EventsConverter.toUpdateCompanyDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Events findById(Long eventId){
        return eventsRepository.findById(eventId).orElseThrow(
                ()-> new EventsCustomException(EventsExceptionCode.NOT_FOUND_EVENT)
        );
    }

    public EventDateRangeDto validateEventDate(
            String startDate, String endDate, EventCrudType crudType) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startEventDate = LocalDateTime.parse(startDate, DATE_TIME_FORMATTER);
        LocalDateTime endEventDate = LocalDateTime.parse(endDate, DATE_TIME_FORMATTER);

        switch (crudType) {
            case CREATE:
                if (!startEventDate.getMonth().equals(now.plusMonths(1).getMonth())) {
                    throw new EventsCustomException(EventsExceptionCode.INVALID_START_DATE);
                }
                break;
            case UPDATE:
                if (startEventDate.isBefore(now)) {
                    throw new EventsCustomException(EventsExceptionCode.INVALID_START_DATE);
                }
                break;
            default:
                throw new EventsCustomException(EventsExceptionCode.INVALID_EVENT_CRUD_TYPE);
        }

        // 2. 시작날이 마지막 날보다 더 빠른지?
        if (!startEventDate.isBefore(endEventDate)) {
            throw new EventsCustomException(EventsExceptionCode.END_DATE_BEFORE_START_DATE);
        }

        return EventsConverter.toEventDateRangeDto(startEventDate, endEventDate);
    }


    private void validateMemberDepartment(
            Members loginMember, MemberDepartment department) {
        if (!department.equals(loginMember.getDepartment())
                && !loginMember.getPosition().equals(MemberPosition.CEO)) {
            throw new EventsCustomException(EventsExceptionCode.INVALID_DEPARTMENT);
        }
    }
}
