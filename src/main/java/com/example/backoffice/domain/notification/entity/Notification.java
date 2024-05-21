package com.example.backoffice.domain.notification.entity;

import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Entity
@Builder
@Document(collection = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends CommonEntity {

    @Id
    private String id;

    private String toMemberName;

    private String fromMemberName;

    private Boolean isRead;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public void isRead(){
        this.isRead = true;
    }
}
