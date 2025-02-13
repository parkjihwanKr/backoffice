package com.example.backoffice.domain.favorite.converter;

import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.member.entity.Members;

import java.util.ArrayList;
import java.util.List;

public class FavoritesConverter {

    public static Favorites toEntity(
            Members loginMember, String favoritesUrl, String description){
        return Favorites.builder()
                .member(loginMember)
                .favoritesUrl(favoritesUrl)
                .description(description)
                .build();
    }

    public static FavoritesResponseDto.CreateOneDto toCreateOneDto(
            Favorites favorites){
        return FavoritesResponseDto.CreateOneDto.builder()
                .favoritesId(favorites.getId())
                .favoritesUrl(favorites.getFavoritesUrl())
                .description(favorites.getDescription())
                .createdAt(favorites.getCreatedAt())
                .modifiedAt(favorites.getModifiedAt())
                .build();
    }

    public static FavoritesResponseDto.ReadOneDto toReadOneDto(
            Favorites favorites){
        return FavoritesResponseDto.ReadOneDto.builder()
                .favoritesId(favorites.getId())
                .favoritesUrl(favorites.getFavoritesUrl())
                .description(favorites.getDescription())
                .createdAt(favorites.getCreatedAt())
                .modifiedAt(favorites.getModifiedAt())
                .build();
    }

    public static List<FavoritesResponseDto.ReadOneDto> toReadAllDto(
            List<Favorites> favoriteList){
        List<FavoritesResponseDto.ReadOneDto> responseDtoList = new ArrayList<>();
        for(Favorites favorites : favoriteList){
            responseDtoList.add(
                    FavoritesConverter.toReadOneDto(favorites));
        }
        return responseDtoList;
    }

    public static List<FavoritesResponseDto.ReadSummaryOneDto> toReadSummaryListDto(
            List<Favorites> favoritesList){
        return favoritesList.stream()
                .map(FavoritesConverter::toReadSummaryOneDto)
                .toList();
    }

    public static FavoritesResponseDto.ReadSummaryOneDto toReadSummaryOneDto(
            Favorites favorites){
        return FavoritesResponseDto.ReadSummaryOneDto.builder()
                .favoritesId(favorites.getId())
                .favoritesUrl(favorites.getFavoritesUrl())
                .description(favorites.getDescription())
                .build();
    }

    public static FavoritesResponseDto.UpdateOneDto toUpdateOneDto(
            Favorites favorites){
        return FavoritesResponseDto.UpdateOneDto.builder()
                .favoritesId(favorites.getId())
                .favoritesUrl(favorites.getFavoritesUrl())
                .description(favorites.getDescription())
                .build();
    }
}
