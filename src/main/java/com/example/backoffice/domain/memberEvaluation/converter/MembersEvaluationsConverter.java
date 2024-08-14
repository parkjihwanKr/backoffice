package com.example.backoffice.domain.memberEvaluation.converter;

import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;

public class MembersEvaluationsConverter {

    public static MembersEvaluations toEntity(Members member, Evaluations evaluation){
        return MembersEvaluations.builder()
                .member(member)
                .evaluation(evaluation)
                .isCompleted(false)
                .build();
    }
}
