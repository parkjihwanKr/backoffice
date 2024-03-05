package com.example.backoffice.domain.like.repository;

import com.example.backoffice.domain.like.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
