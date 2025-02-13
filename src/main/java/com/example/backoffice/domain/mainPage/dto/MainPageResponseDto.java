package com.example.backoffice.domain.mainPage.dto;

import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MainPageResponseDto",
        description = "메인 페이지 응답 DTO")
public class MainPageResponseDto {

    private List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList;
    private List<BoardsResponseDto.ReadSummaryOneDto> generalBoardDtoList;
    private List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardDtoList;
    private List<EventsResponseDto.ReadCompanySummaryOneDto> comapnyEventDtoList;
    private List<VacationsResponseDto.ReadSummaryOneDto> personalVacationDtoList;
    private List<AttendancesResponseDto.ReadSummaryOneDto> personalAttendanceDtoList;
}
