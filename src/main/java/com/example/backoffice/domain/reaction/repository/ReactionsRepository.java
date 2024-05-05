package com.example.backoffice.domain.reaction.repository;

import com.example.backoffice.domain.reaction.entity.Reactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionsRepository extends JpaRepository<Reactions, Long> {
}
