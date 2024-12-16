package com.example.backoffice.domain.favorite.service;

import com.example.backoffice.domain.favorite.dto.FavoritesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface FavoritesServiceV1 {

    /**
     *
     * @param loginMember
     * @param requestDto
     * @return
     */
    FavoritesResponseDto.CreateOneDto createOne(
            Members loginMember, FavoritesRequestDto.CreateOneDto requestDto);

    /**
     *
     * @param favoriteId
     * @param loginMember
     * @return
     */
    FavoritesResponseDto.ReadOneDto readOne(
            Long favoriteId, Members loginMember);

    /**
     *
     * @param loginMember
     * @return
     */
    List<FavoritesResponseDto.ReadOneDto> readAll(Members loginMember);

    /**
     *
     * @param favoritesId
     * @param requestDto
     * @param loginMember
     * @return
     */
    FavoritesResponseDto.UpdateOneDto updateOne(
            Long favoritesId, FavoritesRequestDto.UpdateOneDto requestDto,
            Members loginMember);

    /**
     *
     * @param favoritesId
     * @param loginMember
     */
    void deleteOne(Long favoritesId, Members loginMember);

    /**
     *
     * @param favoriteId
     * @return
     */
    Favorites findById(Long favoriteId);

    /**
     *
     * @param loginMember
     * @return
     */
    List<FavoritesResponseDto.ReadSummaryOneDto> readSummary(
            Members loginMember);
}
