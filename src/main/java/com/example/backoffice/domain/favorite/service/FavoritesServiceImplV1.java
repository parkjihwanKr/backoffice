package com.example.backoffice.domain.favorite.service;

import com.example.backoffice.domain.board.service.BoardsServiceV1;
import com.example.backoffice.domain.comment.service.CommentsServiceV1;
import com.example.backoffice.domain.event.service.EventsServiceV1;
import com.example.backoffice.domain.favorite.converter.FavoritesConverter;
import com.example.backoffice.domain.favorite.dto.FavoritesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.entity.FavoriteType;
import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.favorite.exception.FavoritesCustomException;
import com.example.backoffice.domain.favorite.exception.FavoritesExceptionCode;
import com.example.backoffice.domain.favorite.repository.FavoritesRepository;
import com.example.backoffice.domain.member.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritesServiceImplV1 implements FavoritesServiceV1 {

    private final BoardsServiceV1 boardsService;
    private final EventsServiceV1 eventsService;
    private final CommentsServiceV1 commentsService;
    private final FavoritesRepository favoritesRepository;

    @Override
    @Transactional
    public FavoritesResponseDto.CreateOneDto createOne(
            Members loginMembers, FavoritesRequestDto.CreateOneDto requestDto){

        String targetType = requestDto.getTargetType();
        FavoriteType favoriteType = FavoritesConverter.toFavoriteType(targetType);
        String domainTitle;

        switch (favoriteType) {
            case BOARD ->
                    domainTitle = boardsService.findById(requestDto.getTargetId()).getTitle();
            case EVENT ->
                    domainTitle = eventsService.findById(requestDto.getTargetId()).getTitle();
            case COMMENT, REPLY ->
                    // 댓글과 대댓글은 제목이 없음으로 content를 가져옴
                    domainTitle = commentsService.findById(requestDto.getTargetId()).getContent();
            default -> throw new FavoritesCustomException(FavoritesExceptionCode.INVALID_FAVORITE_TYPE);
        }

        Favorites favorites
                = FavoritesConverter.toEntity(loginMembers, favoriteType, domainTitle);
        favoritesRepository.save(favorites);
        return FavoritesConverter.toCreateOneDto(favorites);
    }

    @Override
    @Transactional(readOnly = true)
    public FavoritesResponseDto.ReadOneDto readOne(
            Long favoriteId, Members loginMember){
        Favorites favorite = favoritesRepository.findByIdAndMember(favoriteId, loginMember).orElseThrow(
                ()-> new FavoritesCustomException(FavoritesExceptionCode.NO_PERMISSION_TO_READ_FAVORITE)
        );
        return FavoritesConverter.toReadOneDto(favorite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoritesResponseDto.ReadOneDto> readAll(
            Members loginMember){
        List<Favorites> favoritieList = favoritesRepository.findAllByMember(loginMember);
        return FavoritesConverter.toReadAllDto(favoritieList);
    }

    @Override
    @Transactional
    public void delete(
            FavoritesRequestDto.DeleteDto requestDto, Members loginMember){
        for (Long favoriteId : requestDto.getFavoriteIdList()) {
            try {
                favoritesRepository.deleteByIdAndMember(favoriteId, loginMember);
            } catch (Exception e) {
                throw new FavoritesCustomException(FavoritesExceptionCode.NO_PERMISSION_TO_DELETE_FAVORITE);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Favorites findById(Long favoriteId){
        return favoritesRepository.findById(favoriteId).orElseThrow(
                ()-> new FavoritesCustomException(FavoritesExceptionCode.NOT_FOUND_FAVORITIES)
        );
    }
}
