package com.example.backoffice.domain.event.converter;

import com.example.backoffice.domain.event.dto.EventDateRangeDto;
import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;

import java.time.LocalDateTime;

public class EventsConverter {

    public static EventDateRangeDto toEventDateRangeDto(
            LocalDateTime startDate, LocalDateTime endDate) {
        return EventDateRangeDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static Events toEntity(
            EventsRequestDto.CreateCompanyEventsRequestDto requestDto,
            EventDateRangeDto eventDateRangeDto, Members member){
        return Events.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .member(member)
                .isDeleted(false)
                .startDate(eventDateRangeDto.getStartDate())
                .endDate(eventDateRangeDto.getEndDate())
                .build();
    }

    public static EventsResponseDto.CreateCompanyEventResponseDto toCreateCompanyDto(Events event){
        return EventsResponseDto.CreateCompanyEventResponseDto.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
