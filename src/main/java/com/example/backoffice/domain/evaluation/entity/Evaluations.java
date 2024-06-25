package com.example.backoffice.domain.evaluation.entity;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Table
@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Evaluations extends CommonEntity {

    // field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer year;

    @NotNull
    private Integer quarter;

    private String title;

    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberDepartment department;

    @OneToMany
    private List<Members> memberList;

    private LocalDateTime startDate;

    private LocalDateTime endTime;

    // relation

    // entity method

}
