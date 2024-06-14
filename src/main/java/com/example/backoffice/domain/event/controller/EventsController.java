package com.example.backoffice.domain.event.controller;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.facade.EventsServiceFacadeV1;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EventsController {

    private final EventsServiceFacadeV1 eventsServiceFacade;

    // 회사 일정 한개 상세 조회
    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventsResponseDto.ReadCompanyEventResponseDto> readCompanyEvent(
            @PathVariable Long eventId){
        EventsResponseDto.ReadCompanyEventResponseDto responseDto
                = eventsServiceFacade.readCompanyEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 회사 일정 1달 조회
    @GetMapping("/events/years/{year}/months/{month}")
    public ResponseEntity<List<EventsResponseDto.ReadCompanyEventResponseDto>> readCompanyMonthEvent(
            @PathVariable Long year, @PathVariable Long month){
        List<EventsResponseDto.ReadCompanyEventResponseDto> responseDtoList
                = eventsServiceFacade.readCompanyMonthEvent(year, month);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 회사 일정 1년 조회, 캘린더 1~12월을 한 번에 출력
    @GetMapping("/events/years/{year}")
    public ResponseEntity<List<List<EventsResponseDto.ReadCompanyEventResponseDto>>> readCompanyYearEvent(
            @PathVariable Long year){
        List<List<EventsResponseDto.ReadCompanyEventResponseDto>> responseDto
                = eventsServiceFacade.readCompanyYearEvent(year);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 부서 일정 부분 수정
    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventsResponseDto.UpdateDepartmentEventResponseDto> updateDepartmentEvent(
            @PathVariable Long eventId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EventsRequestDto.UpdateDepartmentEventRequestDto requestDto){
        EventsResponseDto.UpdateDepartmentEventResponseDto responseDto
                = eventsServiceFacade.updateDepartmentEvent(eventId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 부서 일정 1달 생성
    @PostMapping("/events")
    public ResponseEntity<EventsResponseDto.CreateDepartmentEventResponseDto> createDepartmentEvent(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @ModelAttribute EventsRequestDto.CreateDepartmentEventsRequestDto requestDto){
        EventsResponseDto.CreateDepartmentEventResponseDto responseDto
                = eventsServiceFacade.createDepartmentEvent(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 부서 일정 부분 삭제
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteDepartmentEvent(
            @PathVariable Long eventId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        eventsServiceFacade.deleteDepartmentEvent(eventId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "부서 일정 삭제 성공", 200
                )
        );
    }
    // 멤버 개인 휴가 생성
    @PostMapping("/vacations")
    public ResponseEntity<EventsResponseDto.CreateVacationResponseDto> createVacationEvent(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EventsRequestDto.CreateVacationRequestDto requestDto){
        EventsResponseDto.CreateVacationResponseDto responseDto =
                eventsServiceFacade.createVacationEvent(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 1달 휴가 일정 조회
    @GetMapping("/vacations/years/{year}/months/{month}")
    public ResponseEntity<List<EventsResponseDto.ReadVacationResponseDto>> readVacationMonthEvent(
            @PathVariable Long year, @PathVariable Long month,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<EventsResponseDto.ReadVacationResponseDto> responseDtoList =
                eventsServiceFacade.readVacationMonthEvent(year, month, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
    // 개인 휴가 일정 부분 수정
    @PatchMapping("/vacations/{eventId}")
    public ResponseEntity<EventsResponseDto.UpdateVacationResponseDto> updateVacationEvent(
            @PathVariable Long eventId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody EventsRequestDto.UpdateVacationEventRequestDto requestDto){
        EventsResponseDto.UpdateVacationResponseDto responseDto
                = eventsServiceFacade.updateVacationEvent(
                        eventId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 개인 휴가 일정 부분 삭제
    @DeleteMapping("/vacations/{eventId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteVacationEvent(
            @PathVariable Long eventId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        eventsServiceFacade.deleteVacationEvent(eventId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "휴가 등록 취소 성공", 200
                )
        );
    }
    // 해당 날짜 달력 클릭 시, 휴가 나가 있는 인원 조회
    @GetMapping("/vacations/years/{year}/month/{month}/days/{day}")
    public ResponseEntity<List<EventsResponseDto.ReadVacationResponseDto>> readVacationMemberList(
            @PathVariable Long year, @PathVariable Long month, @PathVariable Long day,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<EventsResponseDto.ReadVacationResponseDto> responseDtoList
                = eventsServiceFacade.readVacationMemberList(
                        year, month, day, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
