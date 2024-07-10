package com.example.backoffice.domain.evaluation.entity;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import com.example.backoffice.domain.question.entity.Questions;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evaluations")
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

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private EvaluationType evaluationType;

    // relation
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembersEvaluations> membersEvaluations;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questions> questionList;

    // entity method
    public void update(
            String title, String description,
            LocalDate startDate, LocalDate endDate, Integer year, Integer quarter){
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.year = year;
        this.quarter = quarter;
    }

    public void updateQuestionList(List<Questions> changedQuestionList){
        this.questionList = changedQuestionList;
    }
}
