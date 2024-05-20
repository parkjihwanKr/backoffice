package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.notification.converter.NotificationConverter;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;
import com.example.backoffice.domain.notification.exception.NotificationCustomException;
import com.example.backoffice.domain.notification.exception.NotificationExceptionCode;
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
            Long memberId, Members fromMember){

        Members toMember = membersService.findById(memberId);

        Notification notification = NotificationConverter.toEntity(
                toMember.getMemberName(), fromMember.getMemberName());
        notificationRepository.save(notification);

        return NotificationConverter.toCreateOneDto(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponseDto.ReadNotificationResponseDto readOne(
            Long memberId, String notificationId, Members member){
        // 1. 로그인 사용자와 일치하는지
        membersService.findMember(member, memberId);
        // 2. 해당 알림이 존재하는지
        Notification notification = findById(notificationId);

        notification.isRead();
        return NotificationConverter.toReadOne(notification);
    }

    @Transactional(readOnly = true)
    public Notification findById(String notificationId){
        return notificationRepository.findById(notificationId).orElseThrow(
                ()-> new NotificationCustomException(NotificationExceptionCode.NOT_FOUND_NOTIFICATION)
        );
    }
}
