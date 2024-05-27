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

import java.util.*;

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

        // 자기 자신에게 '사랑해요' -> 이미 ReactionException으로 막혀 있음.
        // 자기 자신의 게시글, 댓글, 대댓글의 '좋아요'는 할 수 있되
        // 알림은 저장하지 않고 자기 자신의 알림에 뜨지 않기에 return null
        if(notificationData.getToMember().getMemberName()
                .equals(notificationData.getFromMember().getMemberName())){
            return null;
        }
        Notification notification
                = generateMessageAndEntity(notificationData, domainType);
        notificationRepository.save(notification);

        sendNotificationToUser(
                notificationData.getToMember().getMemberName(), notification);
        return NotificationConverter.toCreateOneDto(notification);
    }

    @Override
    @Transactional
    public NotificationResponseDto.ReadNotificationResponseDto readOne(
            Long memberId, String notificationId, Members member){
        // 1. 로그인 사용자와 일치하는지
        membersService.findMember(member, memberId);

        // 2. 해당 알림이 존재하는지
        Notification notification
                = notificationRepository.findByIdAndToMemberName(
                        notificationId, member.getMemberName()).orElseThrow(
                ()-> new NotificationCustomException(
                        NotificationExceptionCode.NOT_FOUND_NOTIFICATION)
        );
        notification.isRead();
        // MongoDB는 repository.save를 사용해서 변경을 해야함
        notificationRepository.save(notification);
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

        // 어차피 자기 자신에게는 알림이 안가게 해야하기에 빈 공간에 넣어주는 걸로
        if(requestDto.getExcludedMemberIdList().isEmpty()){
            requestDto.getExcludedMemberIdList().add(member.getId());
        }
        // 문제 상황 :
        // 1. ADMIN(자기 자신) 또한 알림 메세지에 들어감 -> 자기 자신은 알림에 넣지 않는 식으로 변경 o
        // 2. excludedIdList가 null이면 오류가 뜸 -> 빈칸 리스트가 들어갈 수 있게 조정 o
        // 3. Notifcation jpaRepository에 왜 들어가는지 모르겠음. -> test settings 때문
        Map<String, MemberRole> memberNamesAndRoles
                = membersService.findMemberNameListExcludingDepartmentListAndIdList(
                        requestDto.getExcludedMemberRole(), requestDto.getExcludedMemberIdList());

        Set<MemberRole> memberRoleList = new HashSet<>();
        List<Notification> notificationList = new ArrayList<>();
        String message = requestDto.getMessage();

        memberNamesAndRoles.forEach((memberName, memberRole) -> {
            // 메세지를 만든 본인은 알림에 등록되지 않음
            if(!member.getMemberName().equals(memberName)){
                Notification notification = NotificationConverter.toEntity(
                        memberName, // Map의 key
                        member.getMemberName(), // From Member
                        message, // 메시지 내용
                        NotificationType.MEMBER, // 알림 타입
                        memberRole // Map의 value
                );
                notificationRepository.save(notification);
                memberRoleList.add(memberRole);
                notificationList.add(notification);
                sendNotificationToUser(memberName, notification);
            }
        });
        // 해당 memberRoleList 테스트 후, 설정 예정
        return NotificationConverter.toCreateDto(mainAdmin, memberRoleList, notificationList, message);
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
    public Page<NotificationResponseDto.ReadNotificationListResponseDto> readUnreadList(
            Long memberId, Members member, Pageable pageable){
        // 1. 로그인 사용자와 일치하는지
        Members matchedMember
                = membersService.findMember(member, memberId);

        Page<Notification> notificationPage
                = notificationRepository.findByFromMemberNameInAndIsRead(
                        matchedMember.getMemberName(), false, pageable);

        return NotificationConverter.toReadListDto(notificationPage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponseDto.ReadNotificationListResponseDto> readReadList(
            Long memberId, Members member, Pageable pageable){
        // 1. 로그인 사용자와 일치하는지
        Members matchedMember
                = membersService.findMember(member, memberId);

        Page<Notification> notificationPage
                = notificationRepository.findByFromMemberNameInAndIsRead(
                        matchedMember.getMemberName(), true, pageable);

        return NotificationConverter.toReadListDto(notificationPage);
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
                        memberMessage, domainType, notificationData.getFromMember().getRole());
            }
            case BOARD -> {
                String boardMessage
                        = notificationData.getFromMember().getMemberName()
                        + "님께서 게시글 " + notificationData.getBoard().getTitle()
                        + "에 '좋아요' 이모티콘을 사용하셨습니다.";
                yield NotificationConverter.toEntity(
                        notificationData.getToMember().getMemberName(),
                        notificationData.getFromMember().getMemberName(),
                        boardMessage, domainType, notificationData.getFromMember().getRole());
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
                        commentMessage, domainType, notificationData.getFromMember().getRole());
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
                        replyMessage, domainType, notificationData.getFromMember().getRole());
            }
            default -> throw new NotificationCustomException(NotificationExceptionCode.NOT_MATCHED_REACTION_TYPE);
        };
    }

    private void sendNotificationToUser(String toMemberName, Notification notification){
        simpMessagingTemplate.convertAndSendToUser(toMemberName, "/queue/notifications", notification);
    }
}
