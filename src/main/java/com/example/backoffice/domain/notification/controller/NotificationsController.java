package com.example.backoffice.domain.notification.controller;

import com.example.backoffice.domain.notification.dto.NotificationsRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NotificationsController {

    private final NotificationsServiceFacadeV1 notificationsServiceFacade;

    // 알림 단건 조회
    @GetMapping("/members/{memberId}/notifications/{notificationId}")
    public ResponseEntity<CommonResponseDto<NotificationsResponseDto.ReadOneDto>> readOne(
            @PathVariable Long memberId, @PathVariable String notificationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        NotificationsResponseDto.ReadOneDto responseDto
                = notificationsServiceFacade.readOne(memberId, notificationId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        responseDto, "알림 단건 조회 성공", 200
                )
        );
    }
    // 알림 삭제
    @DeleteMapping("/members/{memberId}/notifications")
    public ResponseEntity<CommonResponseDto<List<String>>> delete(
            @PathVariable Long memberId,
            @RequestBody NotificationsRequestDto.DeleteDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        List<String> deleteIdList = notificationsServiceFacade.delete(
                memberId, requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        deleteIdList, "알림 삭제 성공", 200));
    }

    // 알림 리스트 조회
    @GetMapping("/members/{memberId}/notifications")
    public ResponseEntity<Page<NotificationsResponseDto.ReadDto>> read(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<NotificationsResponseDto.ReadDto> responseDto
                = notificationsServiceFacade.read(memberId, memberDetails.getMembers(), pageable);
        return ResponseEntity.ok().body(responseDto);
    }

    // 모든 알림 리스트를 '읽음' 버튼으로 모두 읽음으로 변경
    @PostMapping("/members/{memberId}/notifications/change-is-read-true")
    public ResponseEntity<CommonResponseDto<List<NotificationsResponseDto.ReadAllDto>>> changeIsReadTrue(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        List<NotificationsResponseDto.ReadAllDto> responseDtoList
                = notificationsServiceFacade.changeIsReadTrue(memberId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        responseDtoList, "모든 알림 리스트 읽기 성공", 200
                )
        );
    }
}
