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
import com.example.backoffice.global.redis.service.CacheMainPageService;
import lombok.RequiredArgsConstructor;
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
    private final CacheMainPageService cacheMainPageService;

    @Transactional(readOnly = true)
    public MainPageResponseDto.SummaryExceptBoardDto readSummaryExceptBoard(
            Members loginMember){
        // 1. 개인 즐겨찾기
        List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList
                = favoritesService.readSummary(loginMember);

        // 4. 부서 일정표
        List<EventsResponseDto.ReadCompanySummaryOneDto> companyEventDtoList
                = eventsService.getCompanyEventDtoList(loginMember.getDepartment());

        // 5. 개인 일정표
        List<VacationsResponseDto.ReadSummaryOneDto> personalVacationDtoList
                = vacationsService.getPersonalVacationDtoList(loginMember.getId());

        // 6. 개인 근태표
        List<AttendancesResponseDto.ReadSummaryOneDto> personalAttendanceDtoList
                = attendancesService.getPersonalAttendanceDtoList(loginMember);

        MainPageResponseDto.SummaryExceptBoardDto
                responseDto = MainPageConverter.toSummaryExceptBoardDto(
                    personalFavoritesDtoList, companyEventDtoList,
                    personalVacationDtoList, personalAttendanceDtoList);
        // 7. 직접 캐시 레포지토리에 저장
        cacheMainPageService.save(loginMember.getId(), responseDto);

        return responseDto;
    }

    public MainPageResponseDto.SummaryBoardDto readBoard(Members loginMember){
        // 2. 전체 게시판 ResponseDto
        List<BoardsResponseDto.ReadSummaryOneDto> generalBoardDtoList
                = boardsService.getGeneralBoardDtoList(loginMember);

        // 3. 부서 게시판 ResponseDto
        List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardDtoList
                = boardsService.getDepartmentBoardDtoList(loginMember);

        return MainPageConverter.toSummaryBoardDto(
                generalBoardDtoList, departmentBoardDtoList);
    }
}
