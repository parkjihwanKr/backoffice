package com.example.backoffice.domain.event.facade;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface EventsServiceFacadeV1 {
    EventsResponseDto.CreateDepartmentEventResponseDto createDepartmentEvent(
            Members loginMember, EventsRequestDto.CreateDepartmentEventsRequestDto requestDto);

    EventsResponseDto.ReadCompanyEventResponseDto readCompanyEvent(
            Long eventId);

    List<EventsResponseDto.ReadCompanyEventResponseDto> readCompanyMonthEvent(
            Long year, Long month);

    List<List<EventsResponseDto.ReadCompanyEventResponseDto>> readCompanyYearEvent(
            Long year);

    EventsResponseDto.UpdateDepartmentEventResponseDto updateDepartmentEvent(
            Long eventId, Members loginMember,
            EventsRequestDto.UpdateDepartmentEventRequestDto requestDto);

    void deleteDepartmentEvent(Long eventId, Members loginMember);

    EventsResponseDto.CreateVacationResponseDto createVacationEvent(
            Members loginMember, EventsRequestDto.CreateVacationRequestDto requestDto);

    List<EventsResponseDto.ReadVacationResponseDto> readVacationMonthEvent(
            Long year, Long month, Members loginMember);

    EventsResponseDto.UpdateVacationResponseDto updateVacationEvent(
            Long vacationId, Members loginMember,
            EventsRequestDto.UpdateVacationEventRequestDto requestDto);

    void deleteVacationEvent(Long vacationId, Members loginMember);

    List<EventsResponseDto.ReadVacationResponseDto> readVacationMemberList(
            Long year, Long month, Long day, Members loginMember);

    List<Events> findAllByEventTypeAndEndDateBefore(Long year, Long month, Long day);

    List<Events> findAllByEventTypeAndStartDateBetween(Long year, Long month, Long day);
}
