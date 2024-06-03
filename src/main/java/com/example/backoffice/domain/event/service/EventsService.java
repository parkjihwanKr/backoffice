package com.example.backoffice.domain.event.service;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;

public interface EventsService {

    EventsResponseDto.CreateCompanyEventResponseDto createCompanyEvent(
            Members member, EventsRequestDto.CreateCompanyEventsRequestDto requestDto);

    Events findById(Long eventId);
}
