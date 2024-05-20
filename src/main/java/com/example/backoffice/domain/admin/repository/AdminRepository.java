package com.example.backoffice.domain.admin.repository;

import com.example.backoffice.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
