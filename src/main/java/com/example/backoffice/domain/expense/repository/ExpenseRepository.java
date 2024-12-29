package com.example.backoffice.domain.expense.repository;

import com.example.backoffice.domain.expense.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // 날짜 범위로 지출 내역 조회
    Page<Expense> findByCreatedAtBetween(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // 시작일 이후의 지출 내역 조회
    Page<Expense> findByCreatedAtAfter(
            LocalDateTime startDate, Pageable pageable);

    // 종료일 이전의 지출 내역 조회
    Page<Expense> findByCreatedAtBefore(LocalDateTime endDate, Pageable pageable);
}
