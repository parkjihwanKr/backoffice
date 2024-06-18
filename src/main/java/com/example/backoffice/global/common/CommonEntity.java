package com.example.backoffice.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class CommonEntity {

    @Column(updatable = false)
    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    @Field("modified_at")
    private LocalDateTime modifiedAt;
}
