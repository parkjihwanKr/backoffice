package com.example.backoffice.domain.mainPage.service;

import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.service.BoardsServiceV1;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.service.EventsServiceV1;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.service.FavoritesServiceV1;
import com.example.backoffice.domain.mainPage.converter.MainPageConverter;
import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final FavoritesServiceV1 favoritesService;
    private final BoardsServiceV1 boardsService;
    private final EventsServiceV1 eventsService;
    private final VacationsServiceV1 vacationsService;
    private final AttendancesServiceV1 attendancesService;

    @Cacheable(
            key = "#loginMember.getId()",
            cacheManager = "cacheManagerForCachedData",
            value = "mainPage"
    )
    @Transactional(readOnly = true)
    public MainPageResponseDto read(Members loginMember){
        // 멤버는 100명으로 고정
        // 즐겨 찾기 1명당 10개의 즐겨찾기 보유 가능
        // 전체 게시글 10-50개 정도 있다고 가정
        // 부서 게시글 멤버당 3개~10개 정도 쓴다고 가정
        // 부서당 일정표 1달에 1~5개 일정이 존재, 부서 7개 존재
        // 개인 일정표(휴가) 1달에 1개~2개 있다고 가정
        // 개인 근태표 멤버당 1년에 365개의 근태 기록 존재

        // 1. 개인 즐겨찾기
        List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList
                = favoritesService.readSummary(loginMember);

        // 2. 전체 게시판 ResponseDto
        List<BoardsResponseDto.ReadSummaryOneDto> generalBoardDtoList
                = boardsService.getGeneralBoardDtoList(loginMember);

        // 3. 부서 게시판 ResponseDto
        List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardDtoList
                = boardsService.getDepartmentBoardDtoList(loginMember);

        // 4. 부서 일정표
        List<EventsResponseDto.ReadCompanySummaryOneDto> companyEventDtoList
                = eventsService.getCompanyEventDtoList(loginMember.getDepartment());

        // 5. 개인 일정표
        List<VacationsResponseDto.ReadSummaryOneDto> personalVacationDtoList
                = vacationsService.getPersonalVacationDtoList(loginMember.getId());

        // 6. 개인 근태표
        List<AttendancesResponseDto.ReadSummaryOneDto> personalAttendanceDtoList
                = attendancesService.getPersonalAttendanceDtoList(loginMember);

        return MainPageConverter.toMainPageResponseDto(
                personalFavoritesDtoList, generalBoardDtoList,
                departmentBoardDtoList, companyEventDtoList,
                personalVacationDtoList, personalAttendanceDtoList);
    }
}
