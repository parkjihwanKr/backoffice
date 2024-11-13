package com.example.backoffice.domain.notification.controller;

import com.example.backoffice.domain.notification.dto.NotificationsRequestDto;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class NotificationWebSocketController {

    private final NotificationsServiceFacadeV1 notificationsServiceFacade;

    @MessageMapping("/admins/notifications")
    public void createForAdmin(
            @Payload NotificationsRequestDto.CreateForAdminDto requestDto,
            Principal principal){
        notificationsServiceFacade.createForAdmin(principal.getName(), requestDto);
    }

    @MessageMapping("/admins/notifications/filtered")
    public void createFilteredForAdmin(
            @Payload NotificationsRequestDto.CreateFilteredForAdminDto requestDto,
            Principal principal) {

        notificationsServiceFacade.createFilteredForAdmin(principal.getName(), requestDto);
    }
}

