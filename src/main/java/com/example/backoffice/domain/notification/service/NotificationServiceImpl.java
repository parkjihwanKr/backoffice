package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.admin.entity.Admin;
import com.example.backoffice.domain.admin.service.AdminService;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.notification.converter.NotificationConverter;
import com.example.backoffice.domain.notification.dto.NotificationRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.exception.NotificationCustomException;
import com.example.backoffice.domain.notification.exception.NotificationExceptionCode;
import com.example.backoffice.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;
    private final MembersService membersService;
    private final AdminService adminService;
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
    public Page<NotificationResponseDto.ReadNotificationListResponseDto> readList(
            Long memberId, Members member, Pageable pageable){
        // 1. 로그인 사용자와 일치하는지
        Members matchedMember
                = membersService.findMember(member, memberId);

        Page<Notification> notificationPage = notificationRepository.findByFromMemberNameIn(
                matchedMember.getMemberName(), pageable);

        return NotificationConverter.toReadListDto(notificationPage);
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
        return NotificationConverter.toReadOneDto(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Long memberId, List<String> notificationIds, Members member){
        // 1. 로그인 사용자와 일치하는지
        membersService.findMember(member, memberId);
        // 2. 해당 알림이 존재하는지
        for (String id : notificationIds) {
            String notificationId
                    = findById(id).getId();
            notificationRepository.deleteById(notificationId);
        }
    }

    @Override
    @Transactional
    public NotificationResponseDto.CreateNotificationListResponseDto createAdminNotification(
            Long adminId, Members member,
            NotificationRequestDto.CreateNotificationRequestDto requestDto){
        // 1. 해당 어드민 계정이 맞는지 -> 멤버 검증까지 같이 됨
        Admin mainAdmin = adminService.findById(adminId);
        // 2. excludeMemberRole에 따라 해당 Role은 제외한 멤버 정보를 가져옴
        // 해당 부분은 제외한 역할의 MemberName과 해당 Member의 역할만 가져온 Map
        Map<String, MemberRole> memberNamesAndRoles
                = membersService.findMemberNameListExcludingDepartmentListAndIdList(
                        requestDto.getExcludedMemberRole(), requestDto.getExcludedMemberIdList());

        List<Notification> notificationList = new ArrayList<>();
        memberNamesAndRoles.forEach((memberName, memberRole) -> {
            Notification notification = NotificationConverter.toEntity(
                    memberName, // Map의 key
                    member.getMemberName(), // From Member
                    requestDto.getMessage(), // 메시지 내용
                    NotificationType.MEMBER, // 알림 타입
                    memberRole // Map의 value
            );
            notificationRepository.save(notification);
            notificationList.add(notification);
            sendNotificationToUser(memberName, notification);
        });
        // 해당 memberRoleList 테스트 후, 설정 예정
        return NotificationConverter.toCreateDto(mainAdmin, null, notificationList);
    }

    @Transactional(readOnly = true)
    public Notification findById(String notificationId){
        return notificationRepository.findById(notificationId).orElseThrow(
                ()-> new NotificationCustomException(NotificationExceptionCode.NOT_FOUND_NOTIFICATION)
        );
    }

    private Notification generateMessageAndEntity(
            NotificationData notificationData, NotificationType domainType){
        return switch (domainType) {
            case MEMBER -> {
                String memberMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 '사랑해요' 이모티콘을 사용하셨습니다.";
                yield NotificationConverter.toEntity(
                        notificationData.getToMember().getMemberName(),
                        notificationData.getFromMember().getMemberName(),
                        memberMessage, domainType, notificationData.getToMember().getRole());
            }
            case BOARD -> {
                String boardMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 게시글 " + notificationData.getBoard().getTitle()
                        + "에 '좋아요' 이모티콘을 사용하셨습니다.";
                yield NotificationConverter.toEntity(
                        notificationData.getToMember().getMemberName(),
                        notificationData.getFromMember().getMemberName(),
                        boardMessage, domainType, notificationData.getToMember().getRole());
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
                        commentMessage, domainType, notificationData.getToMember().getRole());
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
                        replyMessage, domainType, notificationData.getToMember().getRole());
            }
            default -> throw new NotificationCustomException(NotificationExceptionCode.NOT_MATCHED_REACTION_TYPE);
        };
    }

    private void sendNotificationToUser(String toMemberName, Notification notification){
        simpMessagingTemplate.convertAndSendToUser(toMemberName, "/queue/notifications", notification);
    }
}
