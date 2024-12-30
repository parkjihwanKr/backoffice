package com.example.backoffice.domain.event.controller;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.facade.EventsServiceFacadeV1;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EventsController {

    private final EventsServiceFacadeV1 eventsServiceFacade;

    // 부서 일정 1달 조회
    @GetMapping("/departments/{department}/events/years/{year}/months/{month}")
    public ResponseEntity<List<EventsResponseDto.ReadOneForDepartmentEventDto>> readForDepartmentMonthEvent(
            @PathVariable String department, @PathVariable Long year,
            @PathVariable Long month, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<EventsResponseDto.ReadOneForDepartmentEventDto> responseDtoList
                = eventsServiceFacade.readForDepartmentMonthEvent(
                        department, year, month, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 부서 일정 생성
    @PostMapping(
            value = "/departments/{department}/events",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EventsResponseDto.CreateOneForDepartmentEventDto> createOneForDepartmentEvent(
            @PathVariable String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid EventsRequestDto.CreateOneForDepartmentEventDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files){
        EventsResponseDto.CreateOneForDepartmentEventDto responseDto
                = eventsServiceFacade.createOneForDepartmentEvent(department, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 부서 일정 부분 수정
    @PatchMapping(
            value = "/departments/{department}/events/{eventId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EventsResponseDto.UpdateOneForDepartmentEventDto> updateOneForDepartmentEvent(
            @PathVariable String department, @PathVariable Long eventId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid EventsRequestDto.UpdateOneForDepartmentEventDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files){
        EventsResponseDto.UpdateOneForDepartmentEventDto responseDto
                = eventsServiceFacade.updateOneForDepartmentEvent(
                        department, eventId, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 부서 일정 부분 삭제
    @DeleteMapping("/departments/{department}/events/{eventId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteOneForDepartmentEvent(
            @PathVariable String department, @PathVariable Long eventId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        eventsServiceFacade.deleteOneForDepartmentEvent(department, eventId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "부서 일정 삭제 성공", 200
                )
        );
    }

    // 멤버 개인 일정표 조회
    @GetMapping("/members/{memberId}/events/years/{year}/month/{month}")
    public ResponseEntity<List<EventsResponseDto.ReadOneForMemberScheduleDto>> readForMemberSchedule(
            @PathVariable Long memberId, @PathVariable Long year,
            @PathVariable Long month, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<EventsResponseDto.ReadOneForMemberScheduleDto> responseDtoList
                = eventsServiceFacade.readForMemberSchedule(
                        memberId, year, month, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 개인 일정 1일 조회
    @GetMapping("/members/{memberId}/events/years/{year}/months/{month}/days/{day}")
    public ResponseEntity<List<EventsResponseDto.ReadOneForMemberScheduleDto>> readForMemberDaySchedule(
            @PathVariable Long memberId, @PathVariable Long year,
            @PathVariable Long month, @PathVariable Long day,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<EventsResponseDto.ReadOneForMemberScheduleDto> responseDtoList
                = eventsServiceFacade.readForMemberDaySchedule(
                        memberId, year, month, day, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
