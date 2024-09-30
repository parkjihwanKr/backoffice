package com.example.backoffice.domain.vacation.repository;

import com.example.backoffice.domain.vacation.entity.Vacations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationsRepository extends JpaRepository<Vacations, Long> {
}
