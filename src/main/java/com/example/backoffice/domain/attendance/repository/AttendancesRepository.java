package com.example.backoffice.domain.attendance.repository;

import com.example.backoffice.domain.attendance.entity.Attendances;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendancesRepository extends JpaRepository<Attendances, Long> {

}
