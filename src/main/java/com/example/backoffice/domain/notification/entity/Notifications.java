package com.example.backoffice.domain.notification.entity;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@Document(collection = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Notifications{

    @Id
    private String id;

    private String toMemberName;

    private String fromMemberName;

    private Boolean isRead;

    private String message;

    private NotificationType notificationType;

    private MemberDepartment fromMemberDepartment;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("modified_at")
    private LocalDateTime modifiedAt;

    public void isRead() {
        this.isRead = true;
    }
}
