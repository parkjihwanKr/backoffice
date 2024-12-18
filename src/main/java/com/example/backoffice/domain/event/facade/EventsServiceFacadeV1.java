package com.example.backoffice.domain.event.facade;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventsServiceFacadeV1 {

    EventsResponseDto.CreateOneForDepartmentEventDto createOneForDepartmentEvent(
            String department, Members loginMember,
            EventsRequestDto.CreateOneForDepartmentEventDto requestDto,
            List<MultipartFile> files);

    List<EventsResponseDto.ReadOneForDepartmentEventDto> readForDepartmentMonthEvent(
        String department, Long year, Long month, Members loginMember);

    EventsResponseDto.UpdateOneForDepartmentEventDto updateOneForDepartmentEvent(
            String department, Long eventId, Members loginMember,
            EventsRequestDto.UpdateOneForDepartmentEventDto requestDto,
            List<MultipartFile> files);

    void deleteOneForDepartmentEvent(String department, Long eventId, Members loginMember);

    List<EventsResponseDto.ReadOneForMemberScheduleDto> readForMemberSchedule(
            Long memberId, Long year, Long month, Members loginMember);

    List<EventsResponseDto.ReadOneForMemberScheduleDto> readForMemberDaySchedule(
            Long memberId, Long year, Long month, Long day, Members loginMember);

}
