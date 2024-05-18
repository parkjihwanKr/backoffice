package com.example.backoffice.domain.notification.controller;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.service.NotificationService;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NotificationController {

    private final NotificationService notificationService;

    // Member Love Notification
    @PostMapping("/members/{memberId}/notifications")
    public ResponseEntity<NotificationResponseDto.CreateNotificationResponseDto> createNotification(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody NotificationRequestDto.CreateNotificationRequestDto requestDto){
        NotificationResponseDto.CreateNotificationResponseDto responseDto
                = notificationService.createNotification(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(responseDto);
    }
    // Board Like Notification

    // Comment Like Notification

    // Co-comment Like Notification

}
