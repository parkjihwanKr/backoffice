package com.example.backoffice.domain.notification.controller;

import com.example.backoffice.domain.notification.dto.NotificationRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.service.NotificationService;
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
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 단건 조회
    @GetMapping("/members/{memberId}/notifications/{notificationId}")
    public ResponseEntity<CommonResponseDto<NotificationResponseDto.ReadNotificationResponseDto>> readOne(
            @PathVariable Long memberId, @PathVariable String notificationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        NotificationResponseDto.ReadNotificationResponseDto responseDto
                = notificationService.readOne(memberId, notificationId, memberDetails.getMembers());
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
            @RequestBody List<String> notificationIds,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        notificationService.deleteNotification(
                memberId, notificationIds, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        null, "알림 삭제 성공", 200
                )
        );
    }
    // 관리자 전용 단체 메세지 전달
    @PostMapping("/admins/{adminId}/notifications")
    public ResponseEntity<CommonResponseDto<NotificationResponseDto.CreateNotificationListResponseDto>> createAdminNotification(
            @PathVariable Long adminId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody NotificationRequestDto.CreateNotificationRequestDto requestDto){
        NotificationResponseDto.CreateNotificationListResponseDto responseDto
                = notificationService.createAdminNotification(
                        adminId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        responseDto, "전체 알림 전송 성공", 200
                )
        );
    }

    // 알림 리스트 조회
    @GetMapping("/members/{memberId}/notifications")
    public ResponseEntity<Page<NotificationResponseDto.ReadNotificationListResponseDto>> readNotificationList(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<NotificationResponseDto.ReadNotificationListResponseDto> responseDto
                = notificationService.readList(memberId, memberDetails.getMembers(), pageable);
        return ResponseEntity.ok().body(responseDto);
    }
    // 읽지 않은 알림 리스트 조회
    @GetMapping("/members/{memberId}/notifications/unread")
    public ResponseEntity<Page<NotificationResponseDto.ReadNotificationListResponseDto>> readUnReadNotificationList(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10) Pageable pageable){
        Page<NotificationResponseDto.ReadNotificationListResponseDto> responseDto
                = notificationService.readUnreadList(
                        memberId, memberDetails.getMembers(), pageable);
        return ResponseEntity.ok().body(responseDto);
    }

    // 읽은 알림 리스트 조회
    @GetMapping("/members/{memberId}/notifications/read")
    public ResponseEntity<Page<NotificationResponseDto.ReadNotificationListResponseDto>> readReadNotificationList(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<NotificationResponseDto.ReadNotificationListResponseDto> responseDto
                = notificationService.readReadList(
                memberId, memberDetails.getMembers(), pageable);
        return ResponseEntity.ok().body(responseDto);
    }
}
