package com.example.backoffice.domain.member.repository;

import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByEmail(String email);

    Members findByMemberName(String memberName);
}
