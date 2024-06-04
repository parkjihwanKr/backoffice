package com.example.backoffice.domain.event.converter;

import com.example.backoffice.domain.event.dto.EventDateRangeDto;
import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventsConverter {

    public static EventDateRangeDto toEventDateRangeDto(
            LocalDateTime startDate, LocalDateTime endDate) {
        return EventDateRangeDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static Events toEntity(
            EventsRequestDto.CreateDepartmentEventsRequestDto requestDto,
            EventDateRangeDto eventDateRangeDto, Members member){
        return Events.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .member(member)
                .startDate(eventDateRangeDto.getStartDate())
                .endDate(eventDateRangeDto.getEndDate())
                .build();
    }

    public static EventsResponseDto.CreateDepartmentEventResponseDto toCreateDepartmentDto(Events event){
        return EventsResponseDto.CreateDepartmentEventResponseDto.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .build();
    }

    public static List<EventsResponseDto.ReadCompanyEventResponseDto> toReadCompanyMonthDto(
            List<Events> eventList){
        List<EventsResponseDto.ReadCompanyEventResponseDto> eventResponseDtoList = new ArrayList<>();
        for (Events events : eventList) {
            eventResponseDtoList.add(
                    EventsResponseDto.ReadCompanyEventResponseDto.builder()
                            .title(events.getTitle())
                            .startDate(events.getStartDate())
                            .endDate(events.getEndDate())
                            .createdAt(events.getCreatedAt())
                            .build()
            );
        }
        return eventResponseDtoList;
    }

    public static List<List<EventsResponseDto.ReadCompanyEventResponseDto>> toReadCompanyYearDto(
            List<Events> eventList) {
        List<List<EventsResponseDto.ReadCompanyEventResponseDto>> yearEvents = new ArrayList<>();

        for (Month month : Month.values()) {
            List<EventsResponseDto.ReadCompanyEventResponseDto> monthlyEvents = new ArrayList<>();
            for (Events event : eventList) {
                if (event.getStartDate().getMonth().equals(month)) {
                    monthlyEvents.add(
                            EventsResponseDto.ReadCompanyEventResponseDto.builder()
                                    .title(event.getTitle())
                                    .description(event.getDescription())
                                    .startDate(event.getStartDate())
                                    .endDate(event.getEndDate())
                                    .createdAt(event.getCreatedAt())
                                    .modifiedAt(event.getModifiedAt())
                                    .build()
                    );
                }
            }
            yearEvents.add(monthlyEvents);
        }
        return yearEvents;
    }

    public static EventsResponseDto.ReadCompanyEventResponseDto toReadCompanyDto(
            Events event){
        return EventsResponseDto.ReadCompanyEventResponseDto.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .modifiedAt(event.getModifiedAt())
                .build();
    }

    public static EventsResponseDto.UpdateDepartmentEventResponseDto toUpdateCompanyDto(
            Events event){
        return EventsResponseDto.UpdateDepartmentEventResponseDto.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .department(event.getDepartment())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .modifiedAt(event.getModifiedAt())
                .build();
    }
}
