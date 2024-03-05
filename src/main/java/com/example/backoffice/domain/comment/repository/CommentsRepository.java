package com.example.backoffice.domain.comment.repository;

import com.example.backoffice.domain.comment.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
