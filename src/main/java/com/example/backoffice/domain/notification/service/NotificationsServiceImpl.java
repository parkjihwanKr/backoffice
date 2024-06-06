package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.entity.Notifications;
import com.example.backoffice.domain.notification.exception.NotificationsCustomException;
import com.example.backoffice.domain.notification.exception.NotificationsExceptionCode;
import com.example.backoffice.domain.notification.repository.NotificationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsServiceImpl implements NotificationsService {

    private final NotificationsRepository notificationRepository;

    @Override
    @Transactional
    public Notifications save(Notifications notification){
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Notifications findByIdAndToMemberName(String notificationId, String memberName){
        return notificationRepository.findByIdAndToMemberName(notificationId, memberName)
                .orElseThrow(
                        ()-> new NotificationsCustomException(NotificationsExceptionCode.NOT_FOUND_NOTIFICATION)
                );
    }

    @Override
    @Transactional
    public void deleteById(String notificationId){
        notificationRepository.deleteById(notificationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notifications> findByToMemberName(String memberName, Pageable pageable){
        return notificationRepository.findByToMemberName(memberName, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notifications> findByToMemberNameAndIsRead(
            String memberName, Boolean isRead, Pageable pageable){
        return notificationRepository.findByToMemberNameAndIsRead(memberName, isRead, pageable);
    }

    @Override
    @Transactional
    public List<Notifications> saveAll(List<Notifications> notificationList){
        return notificationRepository.saveAll(notificationList);
    }

    @Override
    @Transactional
    public Notifications findById(String notificationId){
        return notificationRepository.findById(notificationId).orElseThrow(
                ()-> new NotificationsCustomException(NotificationsExceptionCode.NOT_FOUND_NOTIFICATION)
        );
    }

    @Override
    @Transactional
    public Notifications saveByMemberInfo(
            String fromMemberName, String toMemberName, MemberDepartment fromMemberDepartment){
        Notifications notifications = NotificationsConverter.toEntity(
                toMemberName, fromMemberName,
                toMemberName+"님께서 최근 바뀐 정보가 있습니다.",
                NotificationType.MEMBER, fromMemberDepartment);
        return notificationRepository.save(notifications);
    }
}
