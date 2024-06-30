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

    private LocalDate endTime;

    // relation
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembersEvaluations> membersEvaluations;

    @OneToMany(mappedBy = "evaluations", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questions> questionList;

    // entity method
    public void addQuestion(Questions question){
        questionList.add(question);
    }
}
