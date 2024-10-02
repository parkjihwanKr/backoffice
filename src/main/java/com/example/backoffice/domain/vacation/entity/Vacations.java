package com.example.backoffice.domain.vacation.entity;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vacations extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private VacationType vacationType;

    private String urgentReason;

    private Boolean urgent;

    private Boolean isAccepted;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members onVacationMember;

    public void update(String vacationTitle, String urgentReason,
                LocalDateTime startDate, LocalDateTime endDate, VacationType vacationType){
        this.title = vacationTitle;
        this.urgentReason = urgentReason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.vacationType = vacationType;
    }
}
