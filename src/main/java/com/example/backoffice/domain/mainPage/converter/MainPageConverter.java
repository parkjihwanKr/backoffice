package com.example.backoffice.domain.mainPage.converter;

import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;

import java.util.List;

public class MainPageConverter {

    public static MainPageResponseDto.ReadOneDto toMainPageResponseDto(
            List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList,
            List<BoardsResponseDto.ReadSummaryOneDto> generalBoardList,
            List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardList,
            List<EventsResponseDto.ReadCompanySummaryOneDto> companyEventList,
            List<VacationsResponseDto.ReadSummaryOneDto> personalVacationList,
            List<AttendancesResponseDto.ReadSummaryOneDto> personalAttendanceList){
        return MainPageResponseDto.ReadOneDto.builder()
                .personalFavoritesDtoList(personalFavoritesDtoList)
                .generalBoardDtoList(generalBoardList)
                .departmentBoardDtoList(departmentBoardList)
                .companyEventDtoList(companyEventList)
                .personalVacationDtoList(personalVacationList)
                .personalAttendanceDtoList(personalAttendanceList)
                .build();
    }

    public static MainPageResponseDto.SummaryBoardDto toSummaryBoardDto(
            List<BoardsResponseDto.ReadSummaryOneDto> generalBoardList,
            List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardList){
        return MainPageResponseDto.SummaryBoardDto.builder()
                .generalBoardDtoList(generalBoardList)
                .departmentBoardDtoList(departmentBoardList)
                .build();
    }

    public static MainPageResponseDto.SummaryExceptBoardDto toSummaryExceptBoardDto(
            List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList,
            List<EventsResponseDto.ReadCompanySummaryOneDto> companyEventList,
            List<VacationsResponseDto.ReadSummaryOneDto> personalVacationList,
            List<AttendancesResponseDto.ReadSummaryOneDto> personalAttendanceList){
        return MainPageResponseDto.SummaryExceptBoardDto.builder()
                .personalFavoritesDtoList(personalFavoritesDtoList)
                .companyEventDtoList(companyEventList)
                .personalVacationDtoList(personalVacationList)
                .personalAttendanceDtoList(personalAttendanceList)
                .build();
    }
}
