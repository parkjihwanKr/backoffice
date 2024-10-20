package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.entity.Notifications;
import com.example.backoffice.domain.notification.exception.NotificationsCustomException;
import com.example.backoffice.domain.notification.exception.NotificationsExceptionCode;
import com.example.backoffice.domain.notification.repository.NotificationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsServiceImplV1 implements NotificationsServiceV1 {

    private final NotificationsRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @Transactional
    public Notifications save(Notifications notification) {
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Notifications findByIdAndToMemberName(String notificationId, String memberName) {
        return notificationRepository.findByIdAndToMemberName(notificationId, memberName)
                .orElseThrow(
                        () -> new NotificationsCustomException(NotificationsExceptionCode.NOT_FOUND_NOTIFICATION)
                );
    }

    @Override
    @Transactional
    public void deleteById(String notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notifications> findByToMemberName(String memberName, Pageable pageable) {
        return notificationRepository.findByToMemberName(memberName, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notifications> findByToMemberNameAndIsRead(
            String memberName, Boolean isRead, Pageable pageable) {
        return notificationRepository.findByToMemberNameAndIsRead(memberName, isRead, pageable);
    }

    @Override
    @Transactional
    public List<Notifications> saveAll(List<Notifications> notificationList) {
        return notificationRepository.saveAll(notificationList);
    }

    @Override
    @Transactional
    public Notifications findById(String notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(
                () -> new NotificationsCustomException(NotificationsExceptionCode.NOT_FOUND_NOTIFICATION)
        );
    }

    @Override
    @Transactional
    public Notifications saveForChangeMemberInfo(
            String fromMemberName, String toMemberName,
            MemberDepartment toMemberDepartment, String message) {
        Notifications notifications = NotificationsConverter.toEntity(
                toMemberName, fromMemberName,
                message, NotificationType.MEMBER, toMemberDepartment);
        sendNotificationForUser(notifications.getToMemberName(), notifications);
        return notificationRepository.save(notifications);
    }

    @Override
    public void sendNotificationForUser(String toMemberName, Notifications notification){
        simpMessagingTemplate.convertAndSendToUser(toMemberName, "/queue/notifications", notification);
    }

    @Override
    public Notifications generateMessageAndEntity(
            NotificationData notificationData, NotificationType domainType){
        return switch (domainType) {
            case MEMBER -> {
                String memberMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 '사랑해요' 이모티콘을 사용하셨습니다.";
                yield toEntity(notificationData, memberMessage, domainType);
            }
            case BOARD -> {
                String boardMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 게시글 " + notificationData.getBoard().getTitle()
                        + "에 '좋아요' 이모티콘을 사용하셨습니다.";
                yield toEntity(notificationData, boardMessage, domainType);
            }
            case COMMENT -> {
                String commentMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 게시글 " + notificationData.getBoard().getTitle()
                        + "의 댓글 '" + notificationData.getComment().getContent()
                        + "'에 '좋아요' 이모티콘을 사용하셨습니다.";
                yield toEntity(notificationData, commentMessage, domainType);
            }
            case REPLY -> {
                String replyMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 게시글 " + notificationData.getComment().getBoard().getTitle()
                        + "의 댓글 '" + notificationData.getReply().getContent()
                        + "'에 '좋아요' 이모티콘을 사용하셨습니다.";
                yield toEntity(notificationData, replyMessage, domainType);
            }
            case EVENT -> {
                String eventMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 "+ notificationData.getEvent().getTitle()
                        + "에 대한 일정을 등록하셨습니다.";
                yield toEntity(notificationData, eventMessage, domainType);
            }
            case URGENT_VACATION -> {
                String urgentVacationMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 긴급하게 휴가를 요청하셨습니다.";
                yield toEntity(notificationData, urgentVacationMessage, domainType);
            }
            case URGENT_SERVER_ERROR -> {
                String urgentServerIssueMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 긴급하게 서버 이슈 메세지를 전달하셨습니다. //"
                        + notificationData.getMessage();
                yield toEntity(notificationData, urgentServerIssueMessage, domainType);
            }
            case EVALUATION -> {
                String evaluationMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 "
                        + notificationData.getMessage()
                        + " 작성 요청 알림입니다.";
                yield toEntity(notificationData, evaluationMessage, domainType);
            }
            case UPDATE_EVALUATION -> {
                // "설문 조사 마감 7일 전입니다. 신속히 마무리 해주시길 바랍니다."
                String evaluationMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 "
                        + notificationData.getMessage();
                yield toEntity(notificationData, evaluationMessage, domainType);
            }
            case UPDATE_VACATION_PERIOD -> {
                String updateVacationPeriodMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 "
                        + notificationData.getMessage();
                yield toEntity(notificationData, updateVacationPeriodMessage, domainType);
            }
            case IS_ACCEPTED_VACATION -> {
                String updateIsAcceptedMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 "
                        + notificationData.getMessage();
                yield toEntity(notificationData, updateIsAcceptedMessage, domainType);
            }
            case DELETE_VACATION_FOR_ADMIN -> {
                String deleteVacationMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 밑과 같은 사유로 휴가를 삭제하셨습니다."
                        + notificationData.getMessage();
                yield toEntity(notificationData, deleteVacationMessage, domainType);
            }
            default -> throw new NotificationsCustomException(NotificationsExceptionCode.NOT_MATCHED_REACTION_TYPE);
        };
    }

    private Notifications toEntity(NotificationData notificationData, String message, NotificationType domainType){
        return NotificationsConverter.toEntity(
                notificationData.getFromMember().getMemberName(),
                notificationData.getToMember().getMemberName(),
                message, domainType, notificationData.getFromMember().getDepartment());
    }
}
