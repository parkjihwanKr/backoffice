package com.example.backoffice.domain.memberEvaluation.entity;

import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "members_evaluations")
public class MembersEvaluations extends CommonEntity {

    // fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isCompleted;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Members member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluations_id")
    private Evaluations evaluation;

    // entity method
    public void isCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
    }
}
