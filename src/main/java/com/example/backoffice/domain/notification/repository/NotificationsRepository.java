package com.example.backoffice.domain.notification.repository;

import com.example.backoffice.domain.notification.entity.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationsRepository extends MongoRepository<Notifications, String> {

    Optional<Notifications> findByIdAndToMemberName(
            String notificationId, String toMemberName);

    Page<Notifications> findByToMemberName(
            String toMemberName, Pageable pageable);

    Page<Notifications> findByToMemberNameAndIsRead(
            String toMemberName, Boolean isRead, Pageable pageable);
}

