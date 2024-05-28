package com.example.backoffice.domain.notification.repository;

import com.example.backoffice.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Optional<Notification> findByIdAndToMemberName(
            String notificationId, String toMemberName);

    Page<Notification> findByToMemberName(
            String toMemberName, Pageable pageable);

    Page<Notification> findByToMemberNameAndIsRead(
            String toMemberName, Boolean isRead, Pageable pageable);
}

