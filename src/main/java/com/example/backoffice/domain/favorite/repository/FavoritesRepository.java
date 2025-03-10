package com.example.backoffice.domain.favorite.repository;

import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    List<Favorites> findAllByMemberId(Long loginMemberId);

    Optional<Favorites> findByIdAndMember(Long favoriteId, Members loginMember);

    List<Favorites> findByMemberId(Long memberId);

    Optional<Favorites> findByIdAndMemberId(Long favoritesId, Long memberId);
}
