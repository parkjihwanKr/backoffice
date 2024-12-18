package com.example.backoffice.domain.event.facade;

import com.example.backoffice.domain.event.converter.EventsConverter;
import com.example.backoffice.domain.event.dto.EventDateRangeDto;
import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.event.exception.EventsExceptionCode;
import com.example.backoffice.domain.event.service.EventsServiceV1;
import com.example.backoffice.domain.file.service.FilesServiceV1;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final EventsServiceV1 eventsService;
    private final VacationsServiceV1 vacationsService;

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
        MemberDepartment memberDepartment = membersService.findDepartment(department);

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
                event.getDepartment(), eventDateRangeDto.getDateRange().getStartDate(),
                eventDateRangeDto.getDateRange().getEndDate(), EventType.DEPARTMENT);

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

    public List<EventsResponseDto.ReadOneForMemberScheduleDto> readForMemberSchedule(
            Long memberId, Long year, Long month, Members loginMember) {
        // 멤버 검증 로직
        if (!memberId.equals(loginMember.getId())) {
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_READ_EVENT);
        }

        // 해당하는 기간의 시작일과 종료일 계산
        LocalDateTime startDate
                = LocalDateTime.of(
                        year.intValue(), month.intValue(), 1, 0, 0);
        YearMonth yearMonth
                = YearMonth.of(year.intValue(), month.intValue());
        LocalDateTime endDate
                = LocalDateTime.of(
                        year.intValue(), month.intValue(),
                yearMonth.lengthOfMonth(), 23, 59, 59);

        List<EventsResponseDto.ReadOneForMemberScheduleDto> responseDtoList = new ArrayList<>();

        // 해당 부서의 모든 일정 조회
        List<Events> departmentEventList
                = eventsService.findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
                        EventType.DEPARTMENT, loginMember.getDepartment(), startDate, endDate);

        responseDtoList.addAll(
                EventsConverter.toReadOneForMemberScheduleDto(
                        EventsConverter.toEventResponseDtoListForEvent(departmentEventList)));
        // 3번에서 개인 휴가 등의 이벤트 추가 가능
        List<Vacations> personalVacationList
                = vacationsService.findAcceptedVacationByMemberIdAndDateRange(
                memberId, true, startDate, endDate);
        responseDtoList.addAll(
                EventsConverter.toReadOneForMemberScheduleDto(
                        EventsConverter.toEventResponseDtoListForVacation(personalVacationList)));

        // Response DTO 생성
        return responseDtoList;
    }

    @Override
    @Transactional
    public List<EventsResponseDto.ReadOneForMemberScheduleDto> readForMemberDaySchedule(
            Long memberId, Long year, Long month, Long day, Members loginMember) {

        // 로그인한 사용자가 자신의 일정을 확인하는 것만 허용
        if (!memberId.equals(loginMember.getId())) {
            throw new EventsCustomException(EventsExceptionCode.NO_PERMISSION_TO_READ_EVENT);
        }

        // 조회할 날짜의 시작과 끝을 설정
        LocalDateTime startDate = LocalDateTime.of(year.intValue(), month.intValue(), day.intValue(), 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year.intValue(), month.intValue(), day.intValue(), 23, 59, 59);

        // 1. 부서 일정 조회
        // 부서의 일정 중에서 이벤트의 시작일 또는 종료일이 지정된 범위(startDate, endDate)에 포함되는 이벤트를 조회
        List<Events> eventList = eventsService.findAllByEventTypeAndDepartmentAndStartOrEndDateBetween(
                EventType.DEPARTMENT, loginMember.getDepartment(), startDate, endDate);

        // 2. 개인 일정 조회
        // 이벤트의 시작일 또는 종료일이 지정된 범위(startDate, endDate)에 포함되는 개인 일정을 조회
        List<Vacations> vacationList
                = vacationsService.findAcceptedVacationByMemberIdAndDateRange(
                        memberId, true, startDate, endDate);

        // 3. 결과 리스트 통합
        List<EventsResponseDto.ReadOneForMemberScheduleDto> responseDtoList = new ArrayList<>();

        responseDtoList.addAll(
                EventsConverter.toReadOneForMemberScheduleDto(
                        EventsConverter.toEventResponseDtoListForEvent(eventList)));

        responseDtoList.addAll(
                EventsConverter.toReadOneForMemberScheduleDto(
                        EventsConverter.toEventResponseDtoListForVacation(vacationList)));
        // 통합된 결과를 DTO로 변환하여 반환
        return responseDtoList;
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

        MemberDepartment memberDepartment = department != null ? membersService.findDepartment(department) : null;

        return eventsService.findAllByEventTypeAndDepartmentAndStartOrEndDateBetween(
                eventType, memberDepartment, start, end);
    }
}
