package com.example.backoffice.domain.event.entity;

import com.example.backoffice.domain.member.entity.MemberDepartment;
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

    // fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private MemberDepartment department;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    // entity method
    public void update(
            String title, String description,
            MemberDepartment department, LocalDateTime startDate,
            LocalDateTime endDate, EventType eventType){
        this.title = title;
        this.description = description;
        this.department = department;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventType = eventType;
    }
}
