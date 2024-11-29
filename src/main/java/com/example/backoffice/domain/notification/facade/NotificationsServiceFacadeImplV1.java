package com.example.backoffice.domain.notification.facade;

import com.example.backoffice.domain.member.converter.MembersConverter;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.dto.NotificationsRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.entity.Notifications;
import com.example.backoffice.domain.notification.exception.NotificationsCustomException;
import com.example.backoffice.domain.notification.exception.NotificationsExceptionCode;
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class NotificationsServiceFacadeImplV1 implements NotificationsServiceFacadeV1 {

    private final NotificationsServiceV1 notificationsService;
    private final MembersServiceV1 membersService;

    @Override
    @Transactional
    public void createOne(
            NotificationData notificationData, NotificationType domainType){
        if(notificationData.getToMember().getMemberName()
                .equals(notificationData.getFromMember().getMemberName())){
            return;
        }
        notificationsService.generateEntityAndSendMessage(notificationData, domainType);
    }

    @Override
    @Transactional
    public NotificationsResponseDto.ReadOneDto readOne(
            Long memberId, String notificationId, Members member){
        // 1. 로그인 사용자와 일치하는지
        membersService.matchLoginMember(member, memberId);

        // 2. 해당 알림이 존재하는지
        Notifications notification
                = notificationsService.findByIdAndToMemberName(notificationId, member.getMemberName());
        notification.isRead();

        MemberPosition fromMemberPosition
                = membersService.findByMemberName(
                        notification.getFromMemberName()).getPosition();

        // MongoDB는 repository.save를 사용해서 변경을 해야함
        notificationsService.save(notification);
        return NotificationsConverter.toReadOneDto(notification, fromMemberPosition);
    }

    @Override
    @Transactional
    public List<String> delete(
            Long memberId, NotificationsRequestDto.DeleteDto requestDto,
            Members member){
        // 1. 로그인 사용자와 일치하는지
        membersService.matchLoginMember(member, memberId);
        // 2. 해당 알림이 존재하는지
        List<String> deleteList = new ArrayList<>();
        for (String id : requestDto.getNotificationIds()) {
            String notificationId
                    = notificationsService.findById(id).getId();
            deleteList.add(notificationId);
            notificationsService.deleteById(notificationId);

        }

        return deleteList;
    }

    // CEO의 특별 권한
    @Override
    @Transactional
    public void createForAdmin(
            String memberName, NotificationsRequestDto.CreateForAdminDto requestDto){
        Members ceo = membersService.findByPosition(MemberPosition.CEO);
        validateCurrentMember(ceo.getId(), memberName);

        // 2. 자신을 제외한 모든 멤버에게 보냄
        List<Members> memberListExcluedOneList
                = membersService.findAllExceptLoginMember(ceo.getId());
        for(Members toMember : memberListExcluedOneList){
            notificationsService.generateEntityAndSendMessage(
                    NotificationsConverter.toNotificationData(
                          toMember, ceo, null, null,
                            null, null, requestDto.getMessage()
                    ), NotificationType.ALL_NOTIFICATIONS);
        }

    }

    @Override
    @Transactional
    public void createFilteredForAdmin(
            String adminName,
            NotificationsRequestDto.CreateFilteredForAdminDto requestDto){
        // 0. adminName으로 해당 멤버의 객체 정보를 가져옴
        Members admin = membersService.findCeoByMemberName(adminName);

        List<MemberDepartment> excludedMemberDepartmentList = new ArrayList<>();
        for(String departmentName : requestDto.getExcludedMemberDepartment()){
            excludedMemberDepartmentList.add(
                    MembersConverter.toDepartment(departmentName));
        }

        Map<String, MemberDepartment> memberNamesAndDepartment
                = membersService.findMemberNameListExcludingDepartmentListAndIdList(
                excludedMemberDepartmentList, requestDto.getExcludedMemberIdList());

        Set<MemberDepartment> memberDepartmentSet = new HashSet<>();
        List<Notifications> notificationList = new ArrayList<>();
        String message = requestDto.getMessage();

        memberNamesAndDepartment.forEach((memberName, memberDepartment) -> {
            // 메세지를 만든 본인은 알림에 등록되지 않음
            if(!admin.getMemberName().equals(memberName)){
                Notifications notification = NotificationsConverter.toEntity(
                        memberName, // Map의 key
                        admin.getMemberName(), // From Member
                        message, // 메시지 내용
                        NotificationType.MEMBER, // 알림 타입
                        memberDepartment // Map의 value
                );
                memberDepartmentSet.add(memberDepartment);
                notificationList.add(notification);
                notificationsService.sendNotificationForUser(memberName, notification);
            }
        });

        notificationsService.saveAll(notificationList);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationsResponseDto.ReadDto> read(
            Long memberId, Members member, Pageable pageable){
        // 1. 로그인 사용자와 일치하는지
        Members matchedMember
                = membersService.matchLoginMember(member, memberId);

        Page<Notifications> notificationPage
                = notificationsService.findByToMemberName(
                matchedMember.getMemberName(), pageable);

        return NotificationsConverter.toReadDto(notificationPage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationsResponseDto.ReadDto> readUnread(
            Long memberId, Members member, Pageable pageable){
        // 1. 로그인 사용자와 일치하는지
        Members matchedMember
                = membersService.matchLoginMember(member, memberId);

        Page<Notifications> notificationPage
                = notificationsService.findByToMemberNameAndIsRead(
                matchedMember.getMemberName(), false, pageable);

        return NotificationsConverter.toReadDto(notificationPage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationsResponseDto.ReadDto> readRead(
            Long memberId, Members member, Pageable pageable){
        // 1. 로그인 사용자와 일치하는지
        Members matchedMember
                = membersService.matchLoginMember(member, memberId);

        Page<Notifications> notificationPage
                = notificationsService.findByToMemberNameAndIsRead(
                matchedMember.getMemberName(), true, pageable);

        return NotificationsConverter.toReadDto(notificationPage);
    }

    @Override
    @Transactional
    public List<NotificationsResponseDto.ReadAllDto> readAll(
            Long memberId, Members member){
        // 1. 로그인 사용자와 일치하는지
        Members matchedMember
                = membersService.matchLoginMember(member, memberId);

        List<Notifications> notificationList
                = notificationsService.findByToMemberNameAndIsRead(
                        matchedMember.getMemberName(), false, null)
                .stream().toList();

        notificationList.forEach(Notifications::isRead);

        notificationsService.saveAll(notificationList);

        return NotificationsConverter.toReadAllDto(notificationList);
    }

    private void validateCurrentMember(Long memberId, String ceoName){
        Long ceoId = membersService.findByMemberName(ceoName).getId();
        if(!ceoId.equals(memberId)){
            throw new NotificationsCustomException(
                    NotificationsExceptionCode.RESTRICT_ACCESS);
        }
    }
}