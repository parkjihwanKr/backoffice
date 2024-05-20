package com.example.backoffice.domain.notification.repository;

import com.example.backoffice.domain.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Optional<Notification> findByToMemberName(String toMemberName);
}
