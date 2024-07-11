package com.example.backoffice.domain.memberEvaluation.repository;

import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembersEvaluationsRepository extends JpaRepository<MembersEvaluations, Long> {

    Optional<MembersEvaluations> findByMemberIdAndEvaluationId(Long memberId, Long EvaluationId);

    List<MembersEvaluations> findAllByIsCompleted(Boolean isCompleted);
}