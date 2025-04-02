package com.example.backoffice.domain.notification.entity;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@Document(collection = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@CompoundIndexes({
        @CompoundIndex(name = "toMember_isRead_idx", def = "{'toMemberName': 1, 'isRead': 1}")
})
public class Notifications{

    @Id
    private String id;

    // toMemberName 기준 조회 속도 향상을 위해
    @Indexed
    private String toMemberName;

    private String fromMemberName;

    // toMemberName 기준 조회 속도 향상
    @Indexed
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
