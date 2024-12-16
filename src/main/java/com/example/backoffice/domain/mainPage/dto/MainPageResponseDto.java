package com.example.backoffice.domain.mainPage.dto;

import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainPageResponseDto {

    private List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList;
    private List<BoardsResponseDto.ReadSummaryOneDto> generalBoardDtoList;
    private List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardDtoList;
    /*private List<EventsResponseDto.ReadSummaryOneDto> generalEventDtoList;
    private List<EventsResponseDto.ReadSummaryOneDto> personalEventDtoList;
    private AttendancesResponseDto.ReadSummaryOneDto todayAttendanceDto;*/
}
