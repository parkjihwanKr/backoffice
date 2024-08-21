package com.example.backoffice.domain.favorite.converter;

import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.entity.FavoriteType;
import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.favorite.exception.FavoritesCustomException;
import com.example.backoffice.domain.favorite.exception.FavoritesExceptionCode;
import com.example.backoffice.domain.member.entity.Members;

import java.util.ArrayList;
import java.util.List;

public class FavoritesConverter {

    public static FavoriteType toFavoriteType(String targetType){
        try{
            return FavoriteType.valueOf(targetType.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new FavoritesCustomException(FavoritesExceptionCode.INVALID_FAVORITE_TYPE);
        }
    }

    public static Favorites toEntity(
            Members loginMember, FavoriteType favoriteType, String content){
        return Favorites.builder()
                .favoriteType(favoriteType)
                .member(loginMember)
                .content(content)
                .build();
    }

    public static FavoritesResponseDto.CreateOneDto toCreateOneDto(
            Favorites favorite){
        return FavoritesResponseDto.CreateOneDto.builder()
                .favoriteId(favorite.getId())
                .favoriteType(favorite.getFavoriteType().getDomainType())
                .favoriteContent(favorite.getContent())
                .createdAt(favorite.getCreatedAt())
                .modifiedAt(favorite.getModifiedAt())
                .build();
    }

    public static FavoritesResponseDto.ReadOneDto toReadOneDto(
            Favorites favorite){
        return FavoritesResponseDto.ReadOneDto.builder()
                .favoriteId(favorite.getId())
                .favoriteType(favorite.getFavoriteType())
                .favoriteContent(favorite.getContent())
                .createdAt(favorite.getCreatedAt())
                .modifiedAt(favorite.getModifiedAt())
                .build();
    }

    public static List<FavoritesResponseDto.ReadOneDto> toReadAllDto(
            List<Favorites> favoriteList){
        List<FavoritesResponseDto.ReadOneDto> responseDtoList = new ArrayList<>();
        for(Favorites favorite : favoriteList){
            responseDtoList.add(
                    FavoritesResponseDto.ReadOneDto.builder()
                            .favoriteId(favorite.getId())
                            .favoriteType(favorite.getFavoriteType())
                            .favoriteContent(favorite.getContent())
                            .createdAt(favorite.getCreatedAt())
                            .modifiedAt(favorite.getModifiedAt()).build()
            );
        }
        return responseDtoList;
    }
}
