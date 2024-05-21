package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.notification.converter.NotificationConverter;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.exception.NotificationCustomException;
import com.example.backoffice.domain.notification.exception.NotificationExceptionCode;
import com.example.backoffice.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;
    private final MembersService membersService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @Transactional
    public NotificationResponseDto.CreateNotificationResponseDto createNotification(
            NotificationData notificationData, NotificationType domainType){

        Notification notification
                = generateMessageAndEntity(notificationData, domainType);
        notificationRepository.save(notification);

        sendNotificationToUser(
                notificationData.getToMember().getMemberName(), notification);
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

    public Notification generateMessageAndEntity(
            NotificationData notificationData, NotificationType domainType){
        return switch (domainType) {
            case MEMBER -> {
                String memberMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 '사랑해요' 이모티콘을 사용하셨습니다.";
                yield NotificationConverter.toEntity(
                        notificationData.getToMember().getMemberName(),
                        notificationData.getFromMember().getMemberName(),
                        memberMessage, domainType);
            }
            case BOARD -> {
                String boardMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 게시글 " + notificationData.getBoard().getTitle()
                        + "에 '좋아요' 이모티콘을 사용하셨습니다.";
                yield NotificationConverter.toEntity(
                        notificationData.getToMember().getMemberName(),
                        notificationData.getFromMember().getMemberName(),
                        boardMessage, domainType);
            }
            case COMMENT -> {
                String commentMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 게시글 " + notificationData.getBoard().getTitle()
                        + "의 댓글 '" + notificationData.getComment().getContent()
                        + "'에 '좋아요' 이모티콘을 사용하셨습니다.";
                yield NotificationConverter.toEntity(
                        notificationData.getToMember().getMemberName(),
                        notificationData.getFromMember().getMemberName(),
                        commentMessage, domainType);
            }
            case REPLY -> {
                String replyMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 게시글 " + notificationData.getComment().getBoard().getTitle()
                        + "의 댓글 '" + notificationData.getReply().getContent()
                        + "'에 '좋아요' 이모티콘을 사용하셨습니다.";
                yield NotificationConverter.toEntity(
                        notificationData.getToMember().getMemberName(),
                        notificationData.getFromMember().getMemberName(),
                        replyMessage, domainType);
            }
            default -> throw new NotificationCustomException(NotificationExceptionCode.NOT_MATCHED_REACTION_TYPE);
        };
    }

    private void sendNotificationToUser(String toMemberName, Notification notification){
        simpMessagingTemplate.convertAndSendToUser(toMemberName, "/queue/notifications", notification);
    }
}
