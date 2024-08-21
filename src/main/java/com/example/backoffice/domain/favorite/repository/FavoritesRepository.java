package com.example.backoffice.domain.favorite.repository;

import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    List<Favorites> findAllByMember(Members loginMember);

    Optional<Favorites> findByIdAndMember(Long favoriteId, Members loginMember);

    void deleteByIdAndMember(Long id, Members loginMember);
}
