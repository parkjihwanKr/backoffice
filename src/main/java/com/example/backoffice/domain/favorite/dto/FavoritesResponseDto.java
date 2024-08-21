package com.example.backoffice.domain.favorite.dto;

import com.example.backoffice.domain.favorite.entity.FavoriteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FavoritesResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto{
        private Long favoriteId;
        private String favoriteType;
        private String favoriteContent;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadOneDto{
        private Long favoriteId;
        private FavoriteType favoriteType;
        private String favoriteContent;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}
