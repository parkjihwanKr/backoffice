package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.entity.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationsServiceV1 {

    /**
     * 알림 하나 저장
     * @param notification : 저장할 알림 엔티티
     * @return 저장된 알림
     */
    Notifications save(Notifications notification);

    /**
     * 받은 사람 이름을 통해 해당 알림 하나 조회
     * @param notificationId : 해당하는 알림 아이디
     * @param memberName : 알림을 받은 사람의 이름
     * @return : 해당하는 알림 하나
     */
    Notifications findByIdAndToMemberName(String notificationId, String memberName);

    /**
     * 알림 아이디를 통한 알림 삭제
     * @param notificationId : 알림 아이디
     */
    void deleteById(String notificationId);

    /**
     * 알림을 받은 사람 이름을 통한 알림 페이징 조회
     * @param toMemberName : 받은 사람 이름
     * @param pageable : 페이징
     * @return {@link Page<Notifications>}
     * 알림을 받은 사람 이름을 통한 알림 페이징 조회
     */
    Page<Notifications> findByToMemberName(String toMemberName, Pageable pageable);

    /**
     * 알림을 받은 사람 이름과 '읽음'상태에 따른 알림 페이징 조회
     * @param toMemberName : 알림을 받은 사람 이름
     * @param isRead : '읽음' 상태
     * @param pageable : 페이징
     * @return {@link Page<Notifications>}
     * 해당 조건에 맞는 알림 페이지
     */
    Page<Notifications> findByToMemberNameAndIsRead(
            String toMemberName, Boolean isRead, Pageable pageable);

    /**
     * 해당 알림 리스트를 저장
     * @param notificationList : 알림 리스트
     * @return : 저장된 알림 리스트
     */
    List<Notifications> saveAll(List<Notifications> notificationList);

    /**
     * 알림 아이디를 통해 알림 조회
     * @param notificationId : 알림 아이디
     * @return : 해당하는 알림
     */
    Notifications findById(String notificationId);

    /**
     * 멤버 정보의 변화에 따른 알림 저장
     * @param fromMemberName : 멤버 정보를 바꾼 사람의 이름(** 관리자 : CEO || HR_MANAGER)
     * @param toMemberName : 멤버 정보가 바뀐 사람의 이름
     * @param fromMemberDepartment : 멤버 정보를 바꾼 사람의 부서
     * @param message : 메세지
     * @return 멤버 정보의 변화에 따른 알림
     */
    Notifications saveForChangeMemberInfo(
            String fromMemberName, String toMemberName,
            MemberDepartment fromMemberDepartment, String message);

    /**
     * 알림 전송
     * @param toMemberName : 알림을 받는 사람
     * @param notification : 알림 엔티티
     */
    void sendNotificationForUser(String toMemberName, Notifications notification);

    /**
     * 알림을 엔티티화하고 메세지를 보냄
     * @param notificationData {@link NotificationData}
     * 도메인 이름
     * @param domainType
     * 각각의 역할에 맞는 도메인 형태의 알림을 Enum 형태로 저장
     */
    void generateEntityAndSendMessage(
            NotificationData notificationData, NotificationType domainType);

    /**
     * 외부 도메인에서 해당하는 NotificationData의 정보를 만듦
     * @param toMember : 알림을 받는 멤버
     * @param fromMember : 알림을 보내는 멤버
     * @param board : 게시글 (** required = false)
     * @param comment : 댓글 (** required = false)
     * @param reply : 답글 (** required = false)
     * @param event : 일정 (** required = false)
     * @param message : 메세지
     * @return 알림에 들어갈 주요 도메인 정보
     */
    NotificationData toNotificationData(
            Members toMember, Members fromMember, Boards board,
            Comments comment, Comments reply, Events event, String message);

    NotificationData toNotificationData(
            Members toMember, Members fromMember, String message);
}
