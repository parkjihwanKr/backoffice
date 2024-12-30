package com.example.backoffice.domain.favorite.service;

import com.example.backoffice.domain.favorite.converter.FavoritesConverter;
import com.example.backoffice.domain.favorite.dto.FavoritesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.favorite.exception.FavoritesCustomException;
import com.example.backoffice.domain.favorite.exception.FavoritesExceptionCode;
import com.example.backoffice.domain.favorite.repository.FavoritesRepository;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritesServiceImplV1 implements FavoritesServiceV1 {

    private final MembersServiceV1 membersService;
    private final FavoritesRepository favoritesRepository;

    @Override
    @Transactional
    public FavoritesResponseDto.CreateOneDto createOne(
            Members loginMembers, FavoritesRequestDto.CreateOneDto requestDto){

        if(favoritesRepository.findByMemberId(loginMembers.getId()).size() >= 11){
            throw new FavoritesCustomException(FavoritesExceptionCode.NOT_EXCEED);
        }

        Favorites favorites
                = FavoritesConverter.toEntity(
                        loginMembers, requestDto.getUrl(), requestDto.getDescription());
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
    public FavoritesResponseDto.UpdateOneDto updateOne(
            Long favoritesId, FavoritesRequestDto.UpdateOneDto requestDto,
            Members loginMember) {
        membersService.findById(loginMember.getId());
        Favorites favorites
                = favoritesRepository.findByIdAndMemberId(
                        favoritesId, loginMember.getId()).orElseThrow(
                ()-> new FavoritesCustomException(FavoritesExceptionCode.NOT_FOUND_FAVORITES)
        );

        if(requestDto.getDescription().equals(favorites.getDescription())){
            throw new FavoritesCustomException(FavoritesExceptionCode.EQUALS_DESCRIPTION);
        }

        favorites.update(requestDto.getDescription());

        return FavoritesConverter.toUpdateOneDto(favorites);
    }

    @Override
    @Transactional
    public void deleteOne(Long favoritesId, Members loginMember){
        Favorites favorites = findById(favoritesId);
        if(!favorites.getMember().getId().equals(loginMember.getId())){
            throw new FavoritesCustomException(
                    FavoritesExceptionCode.NO_PERMISSION_TO_DELETE_FAVORITE);
        }
        favoritesRepository.deleteById(favoritesId);
    }

    @Override
    @Transactional(readOnly = true)
    public Favorites findById(Long favoriteId){
        return favoritesRepository.findById(favoriteId).orElseThrow(
                ()-> new FavoritesCustomException(FavoritesExceptionCode.NOT_FOUND_FAVORITES)
        );
    }

    @Override
    @Transactional
    public List<FavoritesResponseDto.ReadSummaryOneDto> readSummary(
            Members loginMember) {
        membersService.findById(loginMember.getId());
        List<Favorites> favoritesList
                = favoritesRepository.findByMemberId(loginMember.getId());

        return FavoritesConverter.toReadSummaryListDto(favoritesList);
    }
}
