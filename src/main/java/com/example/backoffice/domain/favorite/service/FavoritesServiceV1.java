package com.example.backoffice.domain.favorite.service;

import com.example.backoffice.domain.favorite.dto.FavoritesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface FavoritesServiceV1 {

    FavoritesResponseDto.CreateOneDto createOne(
            Members loginMember, FavoritesRequestDto.CreateOneDto requestDto);

    FavoritesResponseDto.ReadOneDto readOne(
            Long favoriteId, Members loginMember);

    List<FavoritesResponseDto.ReadOneDto> readAll(Members loginMember);

    void delete(FavoritesRequestDto.DeleteDto requestDto, Members loginMember);

    Favorites findById(Long favoriteId);
}
