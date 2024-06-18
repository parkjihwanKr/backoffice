package com.example.backoffice.domain.favorite.converter;

import com.example.backoffice.domain.favorite.dto.FavoritiesResponseDto;
import com.example.backoffice.domain.favorite.entity.FavoriteType;
import com.example.backoffice.domain.favorite.entity.Favorities;
import com.example.backoffice.domain.favorite.exception.FavoritiesCustomException;
import com.example.backoffice.domain.favorite.exception.FavoritiesExceptionCode;
import com.example.backoffice.domain.member.entity.Members;

import java.util.ArrayList;
import java.util.List;

public class FavoritiesConverter {

    public static FavoriteType convertToFavoriteType(String targetType){
        try{
            return FavoriteType.valueOf(targetType.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new FavoritiesCustomException(FavoritiesExceptionCode.INVALID_FAVORITE_TYPE);
        }
    }

    public static Favorities toEntity(
            Members loginMember, FavoriteType favoriteType, String content){
        return Favorities.builder()
                .favoriteType(favoriteType)
                .member(loginMember)
                .content(content)
                .build();
    }

    public static FavoritiesResponseDto.CreateFavoriteResponseDto toCreateDto(
            Favorities favorite){
        return FavoritiesResponseDto.CreateFavoriteResponseDto.builder()
                .favoriteType(favorite.getFavoriteType().getDomainType())
                .favoriteContent(favorite.getContent())
                .createdAt(favorite.getCreatedAt())
                .modifiedAt(favorite.getModifiedAt())
                .build();
    }

    public static FavoritiesResponseDto.ReadFavoriteResponseDto toReadOneDto(
            Favorities favorite){
        return FavoritiesResponseDto.ReadFavoriteResponseDto.builder()
                .favoriteType(favorite.getFavoriteType().getDomainType())
                .favoriteContent(favorite.getContent())
                .createdAt(favorite.getCreatedAt())
                .modifiedAt(favorite.getModifiedAt())
                .build();
    }

    public static List<FavoritiesResponseDto.ReadFavoriteResponseDto> toReadListDto(
            List<Favorities> favoriteList){
        List<FavoritiesResponseDto.ReadFavoriteResponseDto> responseDtoList = new ArrayList<>();
        for(Favorities favorite : favoriteList){
            responseDtoList.add(
                    FavoritiesResponseDto.ReadFavoriteResponseDto.builder()
                            .favoriteContent(favorite.getContent())
                            .createdAt(favorite.getCreatedAt())
                            .modifiedAt(favorite.getModifiedAt()).build()
            );
        }
        return responseDtoList;
    }
}
