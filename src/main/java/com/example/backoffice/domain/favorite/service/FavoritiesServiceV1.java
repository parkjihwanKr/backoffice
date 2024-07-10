package com.example.backoffice.domain.favorite.service;

import com.example.backoffice.domain.favorite.dto.FavoritiesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritiesResponseDto;
import com.example.backoffice.domain.favorite.entity.Favorities;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface FavoritiesServiceV1 {

    FavoritiesResponseDto.CreateOneDto createOne(
            Members loginMember, FavoritiesRequestDto.CreateOneDto requestDto);

    FavoritiesResponseDto.ReadOneDto readOne(
            Long favoriteId, Members loginMember);

    List<FavoritiesResponseDto.ReadOneDto> readAll(Members loginMember);

    void delete(FavoritiesRequestDto.DeleteDto requestDto, Members loginMember);

    Favorities findById(Long favoriteId);
}
