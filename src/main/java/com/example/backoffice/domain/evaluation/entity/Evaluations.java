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

import java.time.LocalDate;
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

    // 생각해보니 한 평가에 많은 멤버, 한 멤버는 부서 분기별 평가, 회사 1년 종합 평가 등등을 한다고 가정하면
    // OneToMany관계가 아님 -> ManyToMany 즉, 중간 테이블 필요
    @OneToMany
    private List<Members> memberList;

    /*@OneToMany(mappedBy = "evaluations", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questions> questionList;*/
    private LocalDate startDate;

    private LocalDate endTime;

    // relation

    // entity method

}
