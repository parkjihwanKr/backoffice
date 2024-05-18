package com.example.backoffice.domain.notification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Entity
@Builder
@Document(collection = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    private String id;

    private String message;

    private String toMemberName;

    private String fromMemberName;

    private Boolean isRead;
}
