package com.example.backoffice.domain.finance.repository;

import com.example.backoffice.domain.finance.entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceRepository extends JpaRepository<Finance, Long> {
}
