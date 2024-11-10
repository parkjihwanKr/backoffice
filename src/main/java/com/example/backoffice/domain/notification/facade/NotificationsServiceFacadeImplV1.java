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
        System.out.println("createForAdmin! method start");

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

        System.out.println("createForAdmin! method end");
    }

    @Override
    @Transactional
    public void createFilteredForAdmin(
            String adminName,
            NotificationsRequestDto.CreateFilteredForAdminDto requestDto){
        System.out.println("test...");
        // 0. adminName으로 해당 멤버의 객체 정보를 가져옴
        Members admin = membersService.findByMemberName(adminName);

        // 1. 해당 어드민 계정이 맞는지 -> 멤버 검증까지 같이 됨
        if(!admin.getPosition().equals(MemberPosition.CEO)){
            throw new NotificationsCustomException(NotificationsExceptionCode.NOT_ACCESS_POSITION);
        }

        // 2. excludeMemberRole에 따라 해당 Role은 제외한 멤버 정보를 가져옴
        // 해당 부분은 제외한 역할의 MemberName과 해당 Member의 역할만 가져온 Map

        // 어차피 자기 자신에게는 알림이 안가게 해야하기에 빈 공간에 넣어주는 걸로
        if(requestDto.getExcludedMemberIdList().isEmpty()){
            requestDto.getExcludedMemberIdList().add(admin.getId());
            System.out.println("예상 시나리오 : true");
        }
        // 문제 상황 :
        // 1. ADMIN(자기 자신) 또한 알림 메세지에 들어감 -> 자기 자신은 알림에 넣지 않는 식으로 변경 o
        // 2. excludedIdList가 null이면 오류가 뜸 -> 빈칸 리스트가 들어갈 수 있게 조정 o
        // 3. Notifcation jpaRepository에 왜 들어가는지 모르겠음. -> test settings 때문
        List<MemberDepartment> excludedMemberDepartmentList = new ArrayList<>();
        for(String departmentName : requestDto.getExcludedMemberDepartment()){
            excludedMemberDepartmentList.add(
                    MembersConverter.toDepartment(departmentName));
        }

        Map<String, MemberDepartment> memberNamesAndDepartment
                = membersService.findMemberNameListExcludingDepartmentListAndIdList(
                excludedMemberDepartmentList, requestDto.getExcludedMemberIdList());

        if(memberNamesAndDepartment.isEmpty()){
            System.out.println("비어있음");
        }

        if(!requestDto.getExcludedMemberIdList().isEmpty()){
            System.out.println("IdList는 안비어있음");
        }

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
                //notificationsService.save(notification);
                memberDepartmentSet.add(memberDepartment);
                notificationList.add(notification);
                notificationsService.sendNotificationForUser(memberName, notification);
            }
        });

        for(Notifications notification : notificationList){
            System.out.println("notification.getMessage() : "+notification.getMessage());
        }

        notificationsService.saveAll(notificationList);
        // 해당 memberRoleList 테스트 후, 설정 예정
        /*NotificationsResponseDto.CreateForAdminDto responseDto
                = NotificationsConverter.toCreateForAdminDto(
                        admin, memberDepartmentSet, notificationList, message);*/
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