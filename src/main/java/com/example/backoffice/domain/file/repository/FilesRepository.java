package com.example.backoffice.domain.file.repository;

import com.example.backoffice.domain.file.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilesRepository extends JpaRepository<Files, Long> {
    void deleteByUrl(String fileUrl);

    List<Files> findByBoardId(Long boardId);

    List<Files> findByEventId(Long eventId);
}
