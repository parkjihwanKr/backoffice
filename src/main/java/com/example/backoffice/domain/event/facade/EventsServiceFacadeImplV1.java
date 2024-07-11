package com.example.backoffice.domain.event.facade;

import com.example.backoffice.domain.event.converter.EventsConverter;
import com.example.backoffice.domain.event.dto.EventDateRangeDto;
import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.EventCrudType;
import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.event.exception.EventsExceptionCode;
import com.example.backoffice.domain.event.service.EventsService;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventsServiceFacadeImplV1 implements EventsServiceFacadeV1{

    private final MembersServiceV1 membersService;
    private final NotificationsServiceFacadeV1 notificationsServiceFacade;
    private final EventsService eventsService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    @Transactional(readOnly = true)
    public EventsResponseDto.ReadOneForCompanyEventDto readOneForCompanyEvent(
            Long eventId){
        Events event = eventsService.findById(eventId);
        return EventsConverter.toReadOneForCompanyEventDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventsResponseDto.ReadOneForCompanyEventDto> readForCompanyMonthEvent(
            Long year, Long month){
        // 해당 날짜의 정보를 가지고 오는데
        // 만약 2024-05-31~06-02 또는 2024-06-23~07-02까지의 프로젝트라면?
        // 해당 이벤트를 가지고 오는지? 아닌지 확인해야함 -> 가져옴
        List<Events> eventList = readMonthEvent(year, month, EventType.DEPARTMENT);

        return EventsConverter.toReadForCompanyMonthEventDto(eventList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<List<EventsResponseDto.ReadOneForCompanyEventDto>> readForCompanyYearEvent(
            Long year){
        LocalDateTime start
                = YearMonth.of(year.intValue(), 1).atDay(1).atStartOfDay();
        LocalDateTime end = YearMonth.of(year.intValue(), 12).atEndOfMonth().atTime(23, 59, 59);
        List<Events> eventList = eventsService.findAllByStartDateBetween(start, end);

        return EventsConverter.toReadForCompanyYearEventDto(eventList);
    }

    @Override
    @Transactional
    public EventsResponseDto.CreateOneForDepartmentEventDto createOneForDepartmentEvent(
            Members loginMember, EventsRequestDto.CreateOneForDepartmentEventDto requestDto){
        membersService.findById(loginMember.getId());

        // 바로 오늘 이전의 날짜가 아니라면 일정을 생성할 수 있게
        validateMemberDepartment(loginMember, requestDto.getDepartment());

        EventDateRangeDto eventDateRangeDto
                = validateEventDate(requestDto.getStartDate(), requestDto.getEndDate());

        Events event = EventsConverter.toEntity(
                requestDto.getTitle(), requestDto.getDescription(),
                eventDateRangeDto, loginMember, EventType.DEPARTMENT);
        eventsService.save(event);
        return EventsConverter.toCreateOneForDepartmentDto(event);
    }

    @Override
    @Transactional
    public EventsResponseDto.UpdateOneForDepartmentEventDto updateOneForDepartmentEvent(
            Long eventId, Members loginMember,
            EventsRequestDto.UpdateOneForDepartmentEventDto requestDto){
        Events event = eventsService.findById(eventId);

        // 로그인 멤버의 부서와 요청한 이벤트를 수행할 부서가 같은지?
        // 최고 경영자는 부서에 영향받지 않고 생성 가능
        validateMemberDepartment(loginMember, requestDto.getDepartment());

        EventDateRangeDto eventDateRangeDto
                = validateEventDate(requestDto.getStartDate(), requestDto.getEndDate());

        event.update(
                requestDto.getTitle(), requestDto.getDescription(),
                requestDto.getDepartment(), eventDateRangeDto.getStartDate(),
                eventDateRangeDto.getEndDate(), EventType.DEPARTMENT);

        return EventsConverter.toUpdateOneForDepartmentEventDto(event);
    }

    @Override
    @Transactional
    public void deleteOneForDepartmentEvent(Long eventId, Members loginMember){
        // 검증
        membersService.findById(loginMember.getId());
        Events event = eventsService.findById(eventId);
        if(!loginMember.getDepartment().equals(event.getDepartment())
                || loginMember.getPosition().equals(MemberPosition.CEO)){
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_DELETE_EVENT);
        }
        eventsService.deleteById(eventId);
    }

    @Override
    @Transactional
    public EventsResponseDto.CreateOneForVacationEventDto createOneForVacationEvent(
            Members loginMember, EventsRequestDto.CreateOneForVacationEventDto requestDto){
        membersService.findById(loginMember.getId());

        EventDateRangeDto eventDateRangeDto
                = validateVacationDate(
                loginMember, requestDto.getStartDate(), requestDto.getEndDate(),
                requestDto.getUrgent());

        String message = loginMember.getMemberName()+"님의 휴가 요청";

        Events event = EventsConverter.toEntity(
                message, requestDto.getReason(),
                eventDateRangeDto, loginMember, EventType.MEMBER_VACATION);

        sendUrgentEventForHRManager(requestDto.getUrgent(), loginMember, event);

        eventsService.save(event);
        return EventsConverter.toCreateOneForVacationEventDto(
                event, requestDto.getUrgent());
    }

    @Override
    @Transactional
    public List<EventsResponseDto.ReadOneForVacationEventDto> readForVacationMonthEvent(
            Long year, Long month, Members loginMember){
        membersService.findById(loginMember.getId());
        List<Events> eventList = readMonthEvent(year, month, EventType.MEMBER_VACATION);
        return EventsConverter.toReadForVacationMonthEventDto(eventList);
    }

    @Override
    @Transactional
    public EventsResponseDto.UpdateOneForVacationEventDto updateOneForVacationEvent(
            Long vacationId, Members loginMember,
            EventsRequestDto.UpdateOneForVacationEventDto requestDto){
        membersService.findById(loginMember.getId());
        Events vacation = eventsService.findById(vacationId);

        // 해당 이벤트의 주인이 로그인한 사람인지?
        validateMemberPermission(
                loginMember, vacation.getMember().getId(), EventCrudType.UPDATE_VACATION);

        EventDateRangeDto eventDateRangeDto = validateVacationDate(
                loginMember, requestDto.getStartDate(),
                requestDto.getEndDate(), requestDto.getUrgent());

        sendUrgentEventForHRManager(requestDto.getUrgent(), loginMember, vacation);

        String vacationTitle = loginMember.getMemberName()+ "님의 휴가 계획";
        vacation.update(vacationTitle, requestDto.getReason(), loginMember.getDepartment(),
                eventDateRangeDto.getStartDate(), eventDateRangeDto.getEndDate(), EventType.MEMBER_VACATION);

        return EventsConverter.toUpdateOneForVacationEventDto(vacation, loginMember.getMemberName());
    }

    @Override
    @Transactional
    public void deleteOneForVacationEvent(Long vacationId, Members loginMember){
        // 검증
        membersService.findById(loginMember.getId());
        eventsService.findById(vacationId);
        validateMemberPermission(loginMember, vacationId, EventCrudType.DELETE_VACATION);

        eventsService.deleteById(vacationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventsResponseDto.ReadMemberForVacationEventDto> readMemberListForVacationEvent(
            Long year, Long month, Long day, Members loginMember) {
        membersService.findById(loginMember.getId());

        List<Events> eventList
                = findAllByEventTypeAndStartDateBetween(year, month, day);

        return EventsConverter.toReadMemberListForVacationEventDto(eventList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Events> findAllByEventTypeAndEndDateBefore(Long year, Long month, Long day) {
        LocalDateTime endOfDay = LocalDateTime.of(
                year.intValue(), month.intValue(), day.intValue(), 23, 59, 59);

        return eventsService.findAllByEventTypeAndEndDateBefore(EventType.MEMBER_VACATION, endOfDay);
    }

    public EventDateRangeDto validateEventDate(String startDate, String endDate) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startEventDate = LocalDateTime.parse(startDate, DATE_TIME_FORMATTER);
        LocalDateTime endEventDate = LocalDateTime.parse(endDate, DATE_TIME_FORMATTER);

        if (startEventDate.isBefore(now)) {
            throw new EventsCustomException(EventsExceptionCode.INVALID_START_DATE);
        }

        // 2. 시작날이 마지막 날보다 더 빠른지?
        if (!startEventDate.isBefore(endEventDate)) {
            throw new EventsCustomException(EventsExceptionCode.END_DATE_BEFORE_START_DATE);
        }

        return EventsConverter.toEventDateRangeDto(startEventDate, endEventDate);
    }

    private EventDateRangeDto validateVacationDate(
            Members loginMember, String startDate, String endDate, Boolean urgent){
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startEventDate = LocalDateTime.parse(startDate, DATE_TIME_FORMATTER);
        LocalDateTime endEventDate = LocalDateTime.parse(endDate, DATE_TIME_FORMATTER);

        // 1. 시작날이 7일 이전 일때 긴급함 표시가 없을 때
        if(startEventDate.isBefore(now.plusDays(7)) && !urgent){
            throw new EventsCustomException(
                    EventsExceptionCode.INVALID_START_DATE_OR_PRESS_URGENT_BUTTON);
        }

        // 2. 시작날이 7일 또는 7일 후일 때 긴급함 표시가 되어 있을 때
        if((startEventDate.isEqual(now.plusDays(7))
                || startEventDate.isAfter(now.plusDays(7))) && urgent){
            throw new EventsCustomException(EventsExceptionCode.INVALID_URGENT);
        }

        // 3. 멤버의 잔여 휴가가 총 일수보다 길 때
        long vacationDays = Duration.between(startEventDate, endEventDate).toDays();
        if(loginMember.getRemainingVacationDays() < (int)vacationDays){
            throw new EventsCustomException(EventsExceptionCode.INSUFFICIENT_VACATION_DAYS);
        }
        // 4. 휴가 총 설정이 30일이 넘어갈 때
        if(vacationDays >= 30){
            throw new EventsCustomException(EventsExceptionCode.INVALID_VACATION_DAYS);
        }

        // 5. 휴가 시작날이 휴가 끝나는 날보다 느릴 때
        if (startEventDate.isBefore(endEventDate)) {
            throw new EventsCustomException(EventsExceptionCode.END_DATE_BEFORE_START_DATE);
        }

        // 6. 전 직원의 휴가율이 30% 이상일 때 -> 휴가를 제한해야함.
        Long memberTotalCount = membersService.findMemberTotalCount();

        // startDate ~ endDate까지 해당 휴가 나가있는 이벤트를 list 출력
        for(long i = 0; i<vacationDays; i++){
            LocalDateTime customStartDate = startEventDate.plusDays(i);
            long vacationingMembersCount
                    = eventsService.countVacationingMembers(customStartDate);

            double vacationRate = (double) (vacationingMembersCount / memberTotalCount);
            // 긴급함 표시가 없고 전 직원 휴가율이 30%이상일 때
            if(vacationRate > 0.3 && !urgent){
                throw new EventsCustomException(EventsExceptionCode.EXCEEDS_VACATION_RATE_LIMIT);
            }
        }
        return EventsConverter.toEventDateRangeDto(startEventDate, endEventDate);
    }

    private void validateMemberDepartment(
            Members loginMember, MemberDepartment department) {
        if (!department.equals(loginMember.getDepartment())
                && !loginMember.getPosition().equals(MemberPosition.CEO)) {
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_CREATE_EVENT);
        }
    }

    private List<Events> readMonthEvent(Long year, Long month, EventType eventType){
        LocalDateTime start
                = YearMonth.of(year.intValue(), month.intValue()).atDay(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1).minusNanos(1);

        return eventsService.findAllByEventTypeAndStartDateBetween(eventType, start, end);
    }

    private void validateMemberPermission(
            Members loginMember, Long vacationMemberId, EventCrudType eventCrudType){
        switch (eventCrudType) {
            case UPDATE_VACATION : {
                if (loginMember.getId().equals(vacationMemberId)) {
                    throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_UPDATE_EVENT);
                }
            }
            case DELETE_VACATION : {
                if (loginMember.getId().equals(vacationMemberId)) {
                    break;
                }else if(loginMember.getPosition().equals(MemberPosition.MANAGER)
                        && loginMember.getDepartment().equals(MemberDepartment.HR)){
                    // 인사 부장이면 모든 멤버의 휴가를 삭제할 권한이 존재
                    break;
                }else{
                    throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_DELETE_EVENT);
                }
            }
            default: throw new EventsCustomException(EventsExceptionCode.INVALID_EVENT_CRUD_TYPE);
        }
    }

    private void sendUrgentEventForHRManager(Boolean urgent, Members loginMember, Events event){
        if(urgent){
            Members hrManager = membersService.findHRManager();
            notificationsServiceFacade.createNotification(
                    NotificationsConverter.toNotificationData(
                            hrManager, loginMember, null, null, null, event, null),
                    NotificationType.URGENT_VACATION_EVENT);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Events> findAllByEventTypeAndStartDateBetween(
            Long year, Long month, Long day) {
        LocalDateTime startOfDay = LocalDateTime.of(
                year.intValue(), month.intValue(), day.intValue(), 0, 0, 0);
        LocalDateTime endOfDay = startOfDay.withHour(23).withMinute(59).withSecond(59);

        return eventsService.findAllByEventTypeAndStartDateBetween(
                EventType.MEMBER_VACATION, startOfDay, endOfDay);
    }
}
