package com.example.backoffice.domain.favorite.service;

import com.example.backoffice.domain.favorite.dto.FavoritesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.favorite.exception.FavoritesCustomException;
import com.example.backoffice.domain.favorite.exception.FavoritesExceptionCode;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface FavoritesServiceV1 {

    /**
     * 즐겨 찾기 생성
     * @param loginMember : 로그인 멤버
     * @param requestDto {@link FavoritesRequestDto.CreateOneDto}
     * 즐겨 찾기 설명, url 요청 DTO
     * @return 즐겨찾기 생성 응답 DTO
     * @throws FavoritesCustomException {@link FavoritesExceptionCode#NOT_EXCEED}
     * 개인당 즐겨찾기 10개 초과한 경우
     */
    FavoritesResponseDto.CreateOneDto createOne(
            Members loginMember, FavoritesRequestDto.CreateOneDto requestDto);

    /**
     * 즐겨찾기 하나 조회
     * @param favoriteId : 조회하려는 즐겨찾기 아이디
     * @param loginMember : 로그인 멤버
     * @return 해당 멤버의 즐겨찾기 한 개의 응답 DTO
     * @throws FavoritesCustomException{@link FavoritesExceptionCode#NOT_FOUND_FAVORITES}
     * 해당 즐겨찾기를 찾을 수 없는 경우
     */
    FavoritesResponseDto.ReadOneDto readOne(
            Long favoriteId, Members loginMember);

    /**
     * 해당 멤버의 모든 즐겨찾기 조회
     * @param loginMember : 로그인 멤버
     * @return 해당 멤버의 모든 즐겨찾기 응답 DTO
     */
    List<FavoritesResponseDto.ReadOneDto> readAll(Members loginMember);

    /**
     * 즐겨 찾기 하나 수정
     * @param favoritesId : 수정할 즐겨찾기 아이디
     * @param requestDto : 수정할 URL 요청 DTO
     * @param loginMember : 로그인 멤버
     * @return 수정할 즐겨 찾기 응답 DTO
     */
    FavoritesResponseDto.UpdateOneDto updateOne(
            Long favoritesId, FavoritesRequestDto.UpdateOneDto requestDto,
            Members loginMember);

    /**
     * 즐겨 찾기 하나 삭제
     * @param favoritesId : 삭제할 즐겨 찾기 아이디
     * @param loginMember : 로그인 멤버
     * @throws FavoritesCustomException
     * {@link FavoritesExceptionCode#NO_PERMISSION_TO_DELETE_FAVORITE}
     * 해당 로그인 멤버가 아닌 다른 사람이 삭제하는 경우
     */
    void deleteOne(Long favoritesId, Members loginMember);

    /**
     * 아이디로 즐겨 찾기 조회
     * @param favoriteId : 조회할 즐겨찾기 아이디
     * @return 해당하는 아이디의 즐겨 찾기 하나
     * @throws FavoritesCustomException
     * {@link FavoritesExceptionCode#NOT_FOUND_FAVORITES}
     * 해당 즐겨찾기를 찾을 수 없음
     */
    Favorites findById(Long favoriteId);

    /**
     * 요약된 즐겨찾기 리스트 조회
     * @param loginMember : 로그인 멤버
     * @return 요약된 즐겨찾기 리스트 응답 DTO
     */
    List<FavoritesResponseDto.ReadSummaryOneDto> readSummary(
            Members loginMember);
}
