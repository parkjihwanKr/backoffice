package com.example.backoffice.domain.notification.controller;

import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.service.NotificationService;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
            @RequestParam List<String> notificationIds,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        notificationService.deleteNotification(
                memberId, notificationIds, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        null, "알림 삭제 성공", 200
                )
        );
    }
    // 알림 리스트 조회

    // 읽지 않은 알림 리스트 조회

    // 읽은 알림 리스트 조회
}
