package com.example.backoffice.domain.event.entity;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Table(name = "events")
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Events extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Members member;
}
