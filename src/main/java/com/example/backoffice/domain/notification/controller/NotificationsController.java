package com.example.backoffice.domain.notification.controller;

import com.example.backoffice.domain.notification.dto.NotificationsRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NotificationsController {

    private final NotificationsServiceFacadeV1 notificationsServiceFacade;

    // 알림 단건 조회
    @GetMapping("/members/{memberId}/notifications/{notificationId}")
    public ResponseEntity<CommonResponseDto<NotificationsResponseDto.ReadNotificationResponseDto>> readOne(
            @PathVariable Long memberId, @PathVariable String notificationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        NotificationsResponseDto.ReadNotificationResponseDto responseDto
                = notificationsServiceFacade.readOne(memberId, notificationId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        responseDto, "알림 단건 조회 성공", 200
                )
        );
    }

    // 알림 삭제
    @DeleteMapping("/members/{memberId}/notifications")
    public ResponseEntity<CommonResponseDto<Void>> deleteNotifications(
            @PathVariable Long memberId,
            @RequestBody NotificationsRequestDto.DeleteNotificationRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        notificationsServiceFacade.deleteNotification(
                memberId, requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        null, "알림 삭제 성공", 200
                )
        );
    }

    // 관리자 전용 단체 메세지 전달
    @PostMapping("/admins/{adminId}/notifications")
    public ResponseEntity<CommonResponseDto<NotificationsResponseDto.CreateNotificationListResponseDto>> createAdminNotification(
            @PathVariable Long adminId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody NotificationsRequestDto.CreateNotificationRequestDto requestDto) {
        NotificationsResponseDto.CreateNotificationListResponseDto responseDto
                = notificationsServiceFacade.createAdminNotification(
                adminId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        responseDto, "전체 알림 전송 성공", 200
                )
        );
    }

    // 알림 리스트 조회
    @GetMapping("/members/{memberId}/notifications")
    public ResponseEntity<Page<NotificationsResponseDto.ReadNotificationListResponseDto>> readNotificationList(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<NotificationsResponseDto.ReadNotificationListResponseDto> responseDto
                = notificationsServiceFacade.readList(memberId, memberDetails.getMembers(), pageable);
        return ResponseEntity.ok().body(responseDto);
    }

    // 읽지 않은 알림 리스트 조회
    @GetMapping("/members/{memberId}/notifications/unread")
    public ResponseEntity<Page<NotificationsResponseDto.ReadNotificationListResponseDto>> readUnReadNotificationList(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<NotificationsResponseDto.ReadNotificationListResponseDto> responseDto
                = notificationsServiceFacade.readUnreadList(
                memberId, memberDetails.getMembers(), pageable);
        return ResponseEntity.ok().body(responseDto);
    }

    // 읽은 알림 리스트 조회
    @GetMapping("/members/{memberId}/notifications/read")
    public ResponseEntity<Page<NotificationsResponseDto.ReadNotificationListResponseDto>> readReadNotificationList(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<NotificationsResponseDto.ReadNotificationListResponseDto> responseDto
                = notificationsServiceFacade.readReadList(
                memberId, memberDetails.getMembers(), pageable);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/members/{memberId}/notifications/unread")
    public ResponseEntity<CommonResponseDto<List<NotificationsResponseDto.ReadNotificationListResponseDto>>> readAll(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        List<NotificationsResponseDto.ReadNotificationListResponseDto> responseDtoList
                = notificationsServiceFacade.readAll(memberId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        responseDtoList, "모든 알림 리스트 읽기 성공", 200
                )
        );
    }
}
