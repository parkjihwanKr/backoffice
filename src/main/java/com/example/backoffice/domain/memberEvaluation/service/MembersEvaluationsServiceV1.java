package com.example.backoffice.domain.memberEvaluation.service;

import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;

import java.util.List;

public interface MembersEvaluationsServiceV1 {

    MembersEvaluations findByMemberIdAndEvaluationId(Long memberId, Long evaluationId);

    void save(MembersEvaluations membersEvaluations);

    List<MembersEvaluations> findAllByIsCompleted(Boolean isCompleted);
}
