package com.example.backoffice.domain.memberEvaluation.service;

import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;

public interface MembersEvaluationsServiceV1 {

    MembersEvaluations findByMemberIdAndEvaluationId(Long memberId, Long evaluationId);

    void save(MembersEvaluations membersEvaluations);
}
