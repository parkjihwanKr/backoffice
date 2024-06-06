package com.example.backoffice.domain.event.converter;

import com.example.backoffice.domain.event.dto.EventDateRangeDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class EventsConverter {

    public static EventDateRangeDto toEventDateRangeDto(
            LocalDateTime startDate, LocalDateTime endDate) {
        return EventDateRangeDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static Events toEntity(
            String title, String description,
            EventDateRangeDto eventDateRangeDto, Members member, EventType eventType){
        return Events.builder()
                .title(title)
                .description(description)
                .member(member)
                .startDate(eventDateRangeDto.getStartDate())
                .endDate(eventDateRangeDto.getEndDate())
                .eventType(eventType)
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

    public static EventsResponseDto.CreateVacationResponseDto toCreateVacationDto(
            Events event, Boolean urgent){
        return EventsResponseDto.CreateVacationResponseDto.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .urgent(urgent)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }

    public static List<EventsResponseDto.ReadVacationResponseDto> toReadVacationMonthDto(
            List<Events> eventList){
        List<EventsResponseDto.ReadVacationResponseDto> responseDtoList = new ArrayList<>();
        for(Events event : eventList){
            responseDtoList.add(
                    EventsResponseDto.ReadVacationResponseDto.builder()
                            .vacationMemberName(event.getMember().getMemberName())
                            .startDate(event.getStartDate())
                            .endDate(event.getEndDate())
                            .build()
            );
        }
        return responseDtoList;
    }

    public static EventsResponseDto.UpdateVacationResponseDto toUpdateVacationDto(
            Events vacation, String memberName){
        return EventsResponseDto.UpdateVacationResponseDto.builder()
                .vacationMemberName(memberName)
                .startDate(vacation.getStartDate())
                .endDate(vacation.getEndDate())
                .build();
    }
}
