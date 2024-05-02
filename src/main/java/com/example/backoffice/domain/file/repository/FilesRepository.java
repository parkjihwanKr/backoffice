package com.example.backoffice.domain.file.repository;

import com.example.backoffice.domain.file.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FilesRepository extends JpaRepository<Files, Long> {
    void deleteByBoardId(Long boardId);
    void deleteByUrl(String fileUrl);
    List<Files> findByBoardId(Long boardId);
}
