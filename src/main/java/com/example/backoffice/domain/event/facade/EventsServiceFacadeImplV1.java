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
import com.example.backoffice.domain.file.service.FilesServiceV1;
import com.example.backoffice.domain.member.converter.MembersConverter;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventsServiceFacadeImplV1 implements EventsServiceFacadeV1{

    private final MembersServiceV1 membersService;
    private final FilesServiceV1 filesService;
    private final NotificationsServiceFacadeV1 notificationsServiceFacade;
    private final EventsService eventsService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    @Transactional
    public EventsResponseDto.CreateOneForCompanyEventDto createOneForCompany(
            EventsRequestDto.CreateOneForCompanyEventDto requestDto ,Members loginMember){

        String message = requestDto.getStartDate() + " / " +requestDto.getEndDate();
        System.out.println(message);
        // 1. 해당 멤버가 회사 일정을 만들 수 있는 권한이 있는지?
        if (!loginMember.getPosition().equals(MemberPosition.CEO) &&
                !loginMember.getPosition().equals(MemberPosition.MANAGER)) {
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_CREATE_EVENT);
        }

        // 2. 요청 날짜 검증
        EventDateRangeDto eventDateRangeDto
                = validateEventDate(requestDto.getStartDate(), requestDto.getEndDate());

        Events event
                = EventsConverter.toEntity(requestDto.getTitle(), requestDto.getDescription(),
                eventDateRangeDto, loginMember, loginMember.getDepartment(), EventType.COMPANY);

        return EventsConverter.toCreateOneForCompanyEventDto(event);
    }

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
        List<Events> eventList = readMonthEvent(year, month, EventType.COMPANY, null);

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
    public EventsResponseDto.UpdateOneForCompanyEventDto updateOneForCompany(
            Long eventId, Members loginMember,
            EventsRequestDto.UpdateOneForCompanyEventDto requestDto,
            List<MultipartFile> files){
        // 1. 일정 존재?
        Events event = eventsService.findById(eventId);

        // 2. 회사 일정인지?
        if (!event.getEventType().equals(EventType.COMPANY)){
            throw new EventsCustomException(EventsExceptionCode.NOT_MATCHED_EVENT_TYPE);
        }

        // 3. 일정 기한이 적절한지?
        EventDateRangeDto eventDateRangeDto
                = validateEventDate(requestDto.getStartDate(), requestDto.getEndDate());

        // 4. event에 fileList가 있었는지?
        List<String> beforeFileUrlList = new ArrayList<>();
        List<String> afterFileUrlList = new ArrayList<>();

        for(int i = 0; i<event.getFileList().size(); i++){
            beforeFileUrlList.add(event.getFileList().get(i).getUrl());
        }
        event.getFileList().clear();
        filesService.deleteForEvent(event.getId(), beforeFileUrlList);

        if(files != null){
            for (MultipartFile file : files) {
                // s3는 수정 관련 메서드가 없기에 제거 후, 재생성하는 방향
                String fileUrl = filesService.createOneForEvent(file, event);
                afterFileUrlList.add(fileUrl);
            }
        }

        event.update(
                requestDto.getTitle(), requestDto.getDescription(),
                event.getDepartment(), eventDateRangeDto.getStartDate(),
                eventDateRangeDto.getEndDate(), EventType.COMPANY);

        return EventsConverter.toUpdateOneForCompanyEventDto(event);
    }

    @Override
    @Transactional
    public void deleteOneForCompany(Long eventId, Members loginMember){
        // 삭제하려는 이벤트를 조회
        Events event = eventsService.findById(eventId);

        // 회사 일정이고 이벤트의 소유자가 아니고, 로그인한 유저가 CEO도 아닌 경우 삭제 권한이 없음을 예외 처리
        if (event.getEventType().equals(EventType.COMPANY)
                && !event.getMember().getId().equals(loginMember.getId())
                && !loginMember.getPosition().equals(MemberPosition.CEO)) {
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_DELETE_EVENT);
        }

        // 이벤트 삭제
        eventsService.deleteById(eventId);
    }

    @Override
    @Transactional
    public EventsResponseDto.CreateOneForDepartmentEventDto createOneForDepartmentEvent(
            String department, Members loginMember,
            EventsRequestDto.CreateOneForDepartmentEventDto requestDto,
            List<MultipartFile> files){
        membersService.findById(loginMember.getId());

        // 바로 오늘 이전의 날짜가 아니라면 일정을 생성할 수 있게
        validateMemberDepartment(department, loginMember);

        EventDateRangeDto eventDateRangeDto
                = validateEventDate(requestDto.getStartDate(), requestDto.getEndDate());

        // 메인 어드민이 부서 일정을 만들 때, HR부서인 메인 어드민이 FIANACE의 일정을 만드려 할 때가
        // 다르게 적용될 가능성이 있기에
        MemberDepartment memberDepartment = MembersConverter.toDepartment(department);

        Events event = EventsConverter.toEntity(
                requestDto.getTitle(), requestDto.getDescription(),
                eventDateRangeDto, loginMember, memberDepartment,
                EventType.DEPARTMENT);

        // 파일 생성
        if(files != null){
            for (MultipartFile file : files){
                filesService.createOneForEvent(file, event);
            }
        }

        eventsService.save(event);

        return EventsConverter.toCreateOneForDepartmentDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventsResponseDto.ReadOneForDepartmentEventDto> readForDepartmentMonthEvent(
            String department, Long year, Long month, Members loginMember){
        // 1. 로그인 멤버가 자기가 속하지 않은 부서에 들어오고자 하면 에러
        if (!loginMember.getDepartment().getDepartment().equals(department)){
            // 1-1. CEO라면 들어갈 수 있음
            if(!loginMember.getPosition().equals(MemberPosition.CEO)){
                throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_READ_EVENT);
            }
        }

        // 2. 해당 조건에 맞는 이벤트들을 가지고 옴
        List<Events> eventList = readMonthEvent(year, month, EventType.DEPARTMENT, department);
        return EventsConverter.toReadForDepartmentMonthEventDto(eventList);
    }

    @Override
    @Transactional
    public EventsResponseDto.UpdateOneForDepartmentEventDto updateOneForDepartmentEvent(
            String department, Long eventId, Members loginMember,
            EventsRequestDto.UpdateOneForDepartmentEventDto requestDto,
            List<MultipartFile> files){
        // 1. 일정 존재?
        Events event = eventsService.findById(eventId);

        // 2. 로그인 멤버의 부서와 요청한 이벤트를 수행할 부서가 같은지?
        // 최고 경영자는 부서에 영향받지 않고 생성 가능
        validateMemberDepartment(department, loginMember);

        // 3. 일정 기한이 적절한지?
        EventDateRangeDto eventDateRangeDto
                = validateEventDate(requestDto.getStartDate(), requestDto.getEndDate());

        // 4. event에 fileList가 있었는지?
        List<String> beforeFileUrlList = new ArrayList<>();
        List<String> afterFileUrlList = new ArrayList<>();

        for(int i = 0; i<event.getFileList().size(); i++){
            beforeFileUrlList.add(event.getFileList().get(i).getUrl());
        }
        event.getFileList().clear();
        filesService.deleteForEvent(event.getId(), beforeFileUrlList);

        if(files != null){
            for (MultipartFile file : files) {
                // s3는 수정 관련 메서드가 없기에 제거 후, 재생성하는 방향
                String fileUrl = filesService.createOneForEvent(file, event);
                afterFileUrlList.add(fileUrl);
            }
        }

        event.update(
                requestDto.getTitle(), requestDto.getDescription(),
                event.getDepartment(), eventDateRangeDto.getStartDate(),
                eventDateRangeDto.getEndDate(), EventType.DEPARTMENT);

        return EventsConverter.toUpdateOneForDepartmentEventDto(event);
    }

    @Override
    @Transactional
    public void deleteOneForDepartmentEvent(String department, Long eventId, Members loginMember){
        // 로그인한 유저가 유효한지 검증
        membersService.findById(loginMember.getId());

        // 삭제하려는 이벤트를 조회
        Events event = eventsService.findById(eventId);

        // 이벤트의 소유자가 아니고, 로그인한 유저가 CEO도 아닌 경우 삭제 권한이 없음을 예외 처리
        if (!loginMember.getDepartment().equals(event.getDepartment())
                && !loginMember.getPosition().equals(MemberPosition.CEO)) {
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_DELETE_EVENT);
        }

        // 이벤트 삭제
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
                eventDateRangeDto, loginMember, loginMember.getDepartment(),
                EventType.MEMBER_VACATION);

        sendUrgentEventForHRManager(requestDto.getUrgent(), loginMember, event);

        eventsService.save(event);
        return EventsConverter.toCreateOneForVacationEventDto(
                event, requestDto.getUrgent());
    }

    public List<EventsResponseDto.ReadOneForMemberScheduleDto> readForMemberSchedule(
            Long memberId, Long year, Long month, Members loginMember) {
        // 멤버 검증 로직
        if (!memberId.equals(loginMember.getId())) {
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_READ_EVENT);
        }

        // 해당하는 기간의 시작일과 종료일 계산
        LocalDateTime startDate = LocalDateTime.of(year.intValue(), month.intValue(), 1, 0, 0);
        YearMonth yearMonth = YearMonth.of(year.intValue(), month.intValue());
        LocalDateTime endDate = LocalDateTime.of(year.intValue(), month.intValue(), yearMonth.lengthOfMonth(), 23, 59, 59);

        // 해당 부서의 모든 일정 조회
        List<Events> departmentEventList
                = eventsService.findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
                        EventType.DEPARTMENT, loginMember.getDepartment(), startDate, endDate);

        // 3번에서 개인 휴가 등의 이벤트 추가 가능
        List<Events> personalEvents = eventsService.findAllByMemberIdAndEventTypeAndDateRange(
                loginMember.getId(), EventType.MEMBER_VACATION, startDate, endDate);
        departmentEventList.addAll(personalEvents);

        // Response DTO 생성
        return EventsConverter.toReadForMemberScheduleDto(departmentEventList);
    }

    @Override
    @Transactional
    public List<EventsResponseDto.ReadOneForMemberScheduleDto> readForMemberDaySchedule(
            Long memberId, Long year, Long month, Long day, Members loginMember) {

        if (memberId.equals(loginMember.getId())) {
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_READ_EVENT);
        }

        LocalDateTime startDate = LocalDateTime.of(year.intValue(), month.intValue(), day.intValue(), 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year.intValue(), month.intValue(), day.intValue(), 23, 59, 59);

        // 개인 일정 조회 (이벤트의 시작일이 범위에 있거나, 종료일이 범위에 있는 이벤트 조회)
        List<Events> personalEvents = eventsService.findAllByMemberIdAndEventTypeAndDateRange(
                memberId, EventType.MEMBER_VACATION, startDate, endDate);

        // 부서 일정 조회 (이벤트의 시작일 또는 종료일이 범위에 있는 이벤트 조회)
        List<Events> departmentEvents = eventsService.findAllByEventTypeAndDepartmentAndStartOrEndDateBetween(
                EventType.DEPARTMENT, loginMember.getDepartment(), startDate, endDate);

        // 결과 리스트 통합
        List<Events> combinedEvents = new ArrayList<>();
        combinedEvents.addAll(personalEvents);
        combinedEvents.addAll(departmentEvents);

        return EventsConverter.toReadForMemberScheduleDto(combinedEvents);
    }

    @Override
    @Transactional
    public List<EventsResponseDto.ReadOneForVacationEventDto> readForVacationMonthEvent(
            Long year, Long month, Members loginMember){
        membersService.findById(loginMember.getId());
        List<Events> eventList = readMonthEvent(year, month, EventType.MEMBER_VACATION, null);
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

        String vacationTitle = loginMember.getName()+ "님의 휴가 계획";
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

        // startDate와 endDate가 'yyyy-MM-dd'T'HH:mm:ss' 형식으로 들어오는 것을 가정
        LocalDateTime startEventDate = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endEventDate = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

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
            String department, Members loginMember) {
        if (!department.equals(loginMember.getDepartment().getDepartment())
                && !loginMember.getPosition().equals(MemberPosition.CEO)) {
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_CREATE_EVENT);
        }
    }

    private List<Events> readMonthEvent(Long year, Long month, EventType eventType, String department) {
        LocalDateTime start = YearMonth.of(year.intValue(), month.intValue()).atDay(1).atStartOfDay();
        LocalDateTime end = YearMonth.of(year.intValue(), month.intValue()).atEndOfMonth().atTime(23, 59, 59);

        MemberDepartment memberDepartment = department != null ? MembersConverter.toDepartment(department) : null;

        return eventsService.findAllByEventTypeAndDepartmentAndStartOrEndDateBetween(
                eventType, memberDepartment, start, end);
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
            notificationsServiceFacade.createOne(
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
