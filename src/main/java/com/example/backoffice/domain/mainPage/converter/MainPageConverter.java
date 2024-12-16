package com.example.backoffice.domain.mainPage.converter;

import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;

import java.util.List;

public class MainPageConverter {

    public static MainPageResponseDto toMainPageResponseDto(
            List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList,
            List<BoardsResponseDto.ReadSummaryOneDto> generalBoardList,
            List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardList,
            List<Events> comapnyEventList, List<Events> personalEventList,
            List<Attendances> personalAttendanceList){ 
        return MainPageResponseDto.builder()
                .personalFavoritesDtoList(personalFavoritesDtoList)
                .generalBoardDtoList(generalBoardList)
                .departmentBoardDtoList(departmentBoardList)
                .build();
    }
}
