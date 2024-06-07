package com.example.backoffice.domain.favorite.dto;

import com.example.backoffice.domain.event.entity.Events;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class FavoritiesResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateFavoriteResponseDto{
        private String favoriteType;
        private String favoriteContent;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadFavoriteResponseDto{
        private String favoriteType;
        private String favoriteContent;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}
