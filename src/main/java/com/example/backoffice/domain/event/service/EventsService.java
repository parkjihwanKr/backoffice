package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface EventsService {

    EventsResponseDto.CreateDepartmentEventResponseDto createDepartmentEvent(
            Members loginMember, EventsRequestDto.CreateDepartmentEventsRequestDto requestDto);

    List<EventsResponseDto.ReadCompanyEventResponseDto> readCompanyMonthEvent(
            Long year, Long month);

    List<List<EventsResponseDto.ReadCompanyEventResponseDto>> readCompanyYearEvent(
            Long year);

    EventsResponseDto.ReadCompanyEventResponseDto readCompanyEvent(
            Long eventId);

    EventsResponseDto.UpdateDepartmentEventResponseDto updateDepartmentEvent(
            Long eventId, Members loginMember,
            EventsRequestDto.UpdateDepartmentEventRequestDto requestDto);

    Events findById(Long eventId);
}
