package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.notification.converter.NotificationConverter;
import com.example.backoffice.domain.notification.dto.NotificationRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;
import com.example.backoffice.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;
    private final MembersService membersService;

    @Override
    @Transactional
    public NotificationResponseDto.CreateNotificationResponseDto createNotification(
            Long memberId, Members fromMember,
            NotificationRequestDto.CreateNotificationRequestDto requestDto){

        log.info("test 1");
        Members toMember = membersService.findById(memberId);

        log.info("test 2");
        Notification notification = NotificationConverter.toEntity(
                toMember.getMemberName(), fromMember.getMemberName(),
                requestDto.getMessage());
        log.info("test 3");
        notificationRepository.save(notification);

        log.info("test 4");
        return NotificationConverter.toCreateNotificationDto(notification);
    }
}
