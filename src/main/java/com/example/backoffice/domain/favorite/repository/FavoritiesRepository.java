package com.example.backoffice.domain.favorite.repository;

import com.example.backoffice.domain.favorite.entity.Favorities;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritiesRepository extends JpaRepository<Favorities,Long> {

    List<Favorities> findAllByMember(Members loginMember);

    Optional<Favorities> findByIdAndMember(Long favoriteId, Members loginMember);

    void deleteByIdAndMember(Long id, Members loginMember);
}
