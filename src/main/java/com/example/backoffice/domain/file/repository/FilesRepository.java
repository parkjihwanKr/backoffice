package com.example.backoffice.domain.file.repository;

import com.example.backoffice.domain.file.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<Files, Long> {

}
