package com.example.backoffice.domain.event.facade;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface EventsServiceFacadeV1 {
    EventsResponseDto.ReadOneForCompanyEventDto readOneForCompanyEvent(Long eventId);

    List<EventsResponseDto.ReadOneForCompanyEventDto> readForCompanyMonthEvent(
            Long year, Long month);

    List<List<EventsResponseDto.ReadOneForCompanyEventDto>> readForCompanyYearEvent(
            Long year);

    EventsResponseDto.CreateOneForDepartmentEventDto createOneForDepartmentEvent(
            Members loginMember, EventsRequestDto.CreateOneForDepartmentEventDto requestDto);

    EventsResponseDto.UpdateOneForDepartmentEventDto updateOneForDepartmentEvent(
            Long eventId, Members loginMember,
            EventsRequestDto.UpdateOneForDepartmentEventDto requestDto);

    void deleteOneForDepartmentEvent(Long eventId, Members loginMember);

    EventsResponseDto.CreateOneForVacationEventDto createOneForVacationEvent(
            Members loginMember, EventsRequestDto.CreateOneForVacationEventDto requestDto);

    List<EventsResponseDto.ReadOneForVacationEventDto> readForVacationMonthEvent(
            Long year, Long month, Members loginMember);

    EventsResponseDto.UpdateOneForVacationEventDto updateOneForVacationEvent(
            Long vacationId, Members loginMember,
            EventsRequestDto.UpdateOneForVacationEventDto requestDto);

    void deleteOneForVacationEvent(Long vacationId, Members loginMember);

    List<EventsResponseDto.ReadMemberForVacationEventDto> readMemberListForVacationEvent(
            Long year, Long month, Long day, Members loginMember);

    List<Events> findAllByEventTypeAndEndDateBefore(Long year, Long month, Long day);

    List<Events> findAllByEventTypeAndStartDateBetween(Long year, Long month, Long day);
}
