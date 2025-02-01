package com.example.backoffice.domain.evaluation.service;

import com.example.backoffice.domain.evaluation.entity.EvaluationType;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.exception.EvaluationsCustomException;
import com.example.backoffice.domain.evaluation.exception.EvaluationsExceptionCode;

import java.time.LocalDate;
import java.util.List;

public interface EvaluationsServiceV1 {

    /**
     * 설문 조사 생성
     * @param evaluation : 저장하려는 설문조사 엔티티
     */
    void save(Evaluations evaluation);

    /**
     * 아이디를 통해 설문조사 조회
     * @param evaluationId : 조회하려는 설문조사의 아이디
     * @return : 해당하는 설문조사
     * @throws EvaluationsCustomException {@link EvaluationsExceptionCode#NOT_FOUND_EVALUATIONS}
     * 해당 아이디로 설문조사를 찾을 수 없는 경우
     */
    Evaluations findById(Long evaluationId);

    /**
     * 아이디를 통해 설문조사 삭제
     * @param evaluationId : 삭제하려는 설문조사의 아이디
     */
    void deleteById(Long evaluationId);

    /**
     * 아이디와 설문조사 타입을 통해 설문조사 조회
     * @param evaluationId : 조회하려는 설문조사 아이디
     * @param evaluationType : 조회하려는 설문조사 타입
     * @return : 해당하는 설문조사
     * @throws EvaluationsCustomException {@link EvaluationsExceptionCode#NOT_FOUND_EVALUATIONS}
     * 해당 아이디와 타입으로 설문조사를 찾을 수 없는 경우
     */

    Evaluations findByIdAndEvaluationType(Long evaluationId, EvaluationType evaluationType);

    /**
     * 오늘로부터 종료일이 7일 이하인 모든 설문조사 조회
     * @param endDate : 마감일
     * @return : 마감일이 7일 이하인 모든 설문조사
     */
    List<Evaluations> findAllByEndDatePlusSevenDays(LocalDate endDate);
}
