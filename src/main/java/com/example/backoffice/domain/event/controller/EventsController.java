package com.example.backoffice.domain.event.controller;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.service.EventsService;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EventsController {

    private final EventsService eventsService;

    // 회사 전체 일정 생성
    @PostMapping("/events")
    public ResponseEntity<EventsResponseDto.CreateCompanyEventResponseDto> createCompanyEvent(
            @ModelAttribute EventsRequestDto.CreateCompanyEventsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        EventsResponseDto.CreateCompanyEventResponseDto responseDto
                = eventsService.createCompanyEvent(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 회사 일정 1달 조회
    // 회사 일정 부분 수정
    // 회사 일정 부분 삭제
    // 회사 일정 1달 삭제
    // 부서 일정 1달 생성
    // 부서 일정 1달 조회
    // 부서 일정 부분 수정
    // 부서 일정 부분 삭제
    // 부서 일정 1달 삭제
    // 1달 휴가 생성
    // 1달 휴가 일정 조회
    // 1달 휴가 일정 부분 수정
    // 1달 휴가 일정 삭제
    // 1달 휴가 일정 부분 삭제
}
