package com.example.backoffice.domain.comment.repository;

import com.example.backoffice.domain.comment.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    // id : childId, parentId : parentId
    Optional<Comments> findByIdAndParentId(Long id, Long parentId);
}
