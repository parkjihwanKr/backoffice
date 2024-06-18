package com.example.backoffice.domain.favorite.service;

import com.example.backoffice.domain.favorite.dto.FavoritiesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritiesResponseDto;
import com.example.backoffice.domain.favorite.entity.Favorities;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface FavoritiesService {

    FavoritiesResponseDto.CreateFavoriteResponseDto createFavorite(
            Members loginMember, FavoritiesRequestDto.CreateFavoriteRequestDto requestDto);

    FavoritiesResponseDto.ReadFavoriteResponseDto readFavorite(
            Long favoriteId, Members loginMember);

    List<FavoritiesResponseDto.ReadFavoriteResponseDto> readFavoriteList(Members loginMember);

    void deleteFavorite(
            FavoritiesRequestDto.DeleteFavoriteIdListRequestDto requestDto, Members loginMember);

    Favorities findById(Long favoriteId);
}
