package com.example.backoffice.domain.favorite.service;

import com.example.backoffice.domain.board.service.BoardsServiceV1;
import com.example.backoffice.domain.comment.service.CommentsServiceV1;
import com.example.backoffice.domain.event.service.EventsService;
import com.example.backoffice.domain.favorite.converter.FavoritiesConverter;
import com.example.backoffice.domain.favorite.dto.FavoritiesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritiesResponseDto;
import com.example.backoffice.domain.favorite.entity.FavoriteType;
import com.example.backoffice.domain.favorite.entity.Favorities;
import com.example.backoffice.domain.favorite.exception.FavoritiesCustomException;
import com.example.backoffice.domain.favorite.exception.FavoritiesExceptionCode;
import com.example.backoffice.domain.favorite.repository.FavoritiesRepository;
import com.example.backoffice.domain.member.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritiesServiceImplV1 implements FavoritiesServiceV1 {

    private final BoardsServiceV1 boardsService;
    private final EventsService eventsService;
    private final CommentsServiceV1 commentsService;
    private final FavoritiesRepository favoritiesRepository;

    @Override
    @Transactional
    public FavoritiesResponseDto.CreateOneDto createOne(
            Members loginMembers, FavoritiesRequestDto.CreateOneDto requestDto){

        String targetType = requestDto.getTargetType();
        FavoriteType favoriteType = FavoritiesConverter.convertToFavoriteType(targetType);
        String domainTitle;

        switch (favoriteType) {
            case BOARD ->
                    domainTitle = boardsService.findById(requestDto.getTargetId()).getTitle();
            case EVENT ->
                    domainTitle = eventsService.findById(requestDto.getTargetId()).getTitle();
            case COMMENT, REPLY ->
                    // 댓글과 대댓글은 제목이 없음으로 content를 가져옴
                    domainTitle = commentsService.findById(requestDto.getTargetId()).getContent();
            default -> throw new FavoritiesCustomException(FavoritiesExceptionCode.INVALID_FAVORITE_TYPE);
        }

        Favorities favorities
                = FavoritiesConverter.toEntity(loginMembers, favoriteType, domainTitle);
        favoritiesRepository.save(favorities);
        return FavoritiesConverter.toCreateOneDto(favorities);
    }

    @Override
    @Transactional(readOnly = true)
    public FavoritiesResponseDto.ReadOneDto readOne(
            Long favoriteId, Members loginMember){
        Favorities favorite = favoritiesRepository.findByIdAndMember(favoriteId, loginMember).orElseThrow(
                ()-> new FavoritiesCustomException(FavoritiesExceptionCode.NO_PERMISSION_TO_READ_FAVORITE)
        );
        return FavoritiesConverter.toReadOneDto(favorite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoritiesResponseDto.ReadOneDto> readAll(
            Members loginMember){
        List<Favorities> favoritieList = favoritiesRepository.findAllByMember(loginMember);
        return FavoritiesConverter.toReadAllDto(favoritieList);
    }

    @Override
    @Transactional
    public void delete(
            FavoritiesRequestDto.DeleteDto requestDto, Members loginMember){
        for (Long favoriteId : requestDto.getFavoriteIdList()) {
            try {
                favoritiesRepository.deleteByIdAndMember(favoriteId, loginMember);
            } catch (Exception e) {
                throw new FavoritiesCustomException(FavoritiesExceptionCode.NO_PERMISSION_TO_DELETE_FAVORITE);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Favorities findById(Long favoriteId){
        return favoritiesRepository.findById(favoriteId).orElseThrow(
                ()-> new FavoritiesCustomException(FavoritiesExceptionCode.NOT_FOUND_FAVORITIES)
        );
    }
}
