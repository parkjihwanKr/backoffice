package com.example.backoffice.domain.notification.repository;

import com.example.backoffice.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Optional<Notification> findByIdAndToMemberName(
            String notificationId, String toMemberName);

    Page<Notification> findByFromMemberNameIn(
            String fromMemberName, Pageable pageable);

    Page<Notification> findByFromMemberNameInAndIsRead(
            String fromMemberName, Boolean isRead, Pageable pageable);
}

