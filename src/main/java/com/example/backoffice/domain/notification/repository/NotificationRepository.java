package com.example.backoffice.domain.notification.repository;

import com.example.backoffice.domain.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

}
