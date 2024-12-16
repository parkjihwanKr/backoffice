package com.example.backoffice.domain.mainPage.service;

import com.example.backoffice.domain.board.converter.BoardsConverter;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.service.BoardsServiceV1;
import com.example.backoffice.domain.board.service.ViewCountServiceV1;
import com.example.backoffice.domain.event.service.EventsServiceV1;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.favorite.service.FavoritesServiceV1;
import com.example.backoffice.domain.mainPage.converter.MainPageConverter;
import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final FavoritesServiceV1 favoritesService;
    private final BoardsServiceV1 boardsService;
    private final ViewCountServiceV1 viewCountService;
    private final EventsServiceV1 eventsService;
    private final VacationsServiceV1 vacationsService;

    @Transactional(readOnly = true)
    public MainPageResponseDto read(Members loginMember){
        // 1. 개인 즐겨찾기
        List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList
                = favoritesService.readSummary(loginMember);
        // 2. 전체 게시판 ResponseDto
        // 해당 부분 연산 속도 궁금하네 ? CreatedAt을 계산해서 가지고 오는건가?
        // 아니면 그냥 createdAt 정렬해서 맨 위의 3개를 가지고 오는 연산인가?
        List<Boards> generalBoardList
                = boardsService.findThreeByCreatedAtDesc(BoardType.GENERAL);
        List<Long> generalBoardViewCountList
                = generalBoardList.stream()
                .map(board ->
                        viewCountService.getTotalViewCountByBoardId(board.getId()))
                .toList();
        List<BoardsResponseDto.ReadSummaryOneDto> generalBoardDtoList
                = BoardsConverter.toReadSummaryListDto(
                        generalBoardList, generalBoardViewCountList);

        // 3. 부서 게시판 ResponseDto
        List<Boards> departmentBoardList
                = boardsService.findThreeByCreatedAtDesc(BoardType.DEPARTMENT);
        List<Long> departmentBoardViewCountList
                = departmentBoardList.stream()
                .map(board ->
                        viewCountService.getTotalViewCountByBoardId(board.getId()))
                .toList();
        List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardDtoList
                = BoardsConverter.toReadSummaryListDto(
                        departmentBoardList, departmentBoardViewCountList);

        // 4. 부서 일정표

        // 5. 개인 일정표

        // 6. 개인 근태표

        return MainPageConverter.toMainPageResponseDto(
                personalFavoritesDtoList, generalBoardDtoList,
                departmentBoardDtoList, null, null, null);
    }
}
