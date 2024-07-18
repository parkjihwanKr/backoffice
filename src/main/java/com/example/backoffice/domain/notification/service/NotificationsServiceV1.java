package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.notification.entity.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationsServiceV1 {

    Notifications save(Notifications notification);

    Notifications findByIdAndToMemberName(String notificationId, String memberName);

    void deleteById(String notificationId);

    Page<Notifications> findByToMemberName(String memberName, Pageable pageable);

    Page<Notifications> findByToMemberNameAndIsRead(
            String memberName, Boolean isRead, Pageable pageable);

    List<Notifications> saveAll(List<Notifications> notificationList);

    Notifications findById(String notificationId);

    Notifications saveForChangeMemberInfo(
            String fromMemberName, String toMemberName, MemberDepartment fromMemberDepartment);
}
