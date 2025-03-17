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

import java.io.Serializable;
import java.util.List;

public class MainPageResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "SummaryExceptBoardDto",
            description = "메인 페이지 요약 응답 DTO")
    public static class SummaryExceptBoardDto implements Serializable {
        private List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList;
        private List<EventsResponseDto.ReadCompanySummaryOneDto> companyEventDtoList;
        private List<VacationsResponseDto.ReadSummaryOneDto> personalVacationDtoList;
        private List<AttendancesResponseDto.ReadSummaryOneDto> personalAttendanceDtoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "SummaryBoardDto",
            description = "메인 페이지 게시글 응답 DTO")
    public static class SummaryBoardDto {
        private List<BoardsResponseDto.ReadSummaryOneDto> generalBoardDtoList;
        private List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardDtoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "ReadOneDto",
            description = "메인 페이지 응답 DTO")
    public static class ReadOneDto {
        private List<FavoritesResponseDto.ReadSummaryOneDto> personalFavoritesDtoList;
        private List<BoardsResponseDto.ReadSummaryOneDto> generalBoardDtoList;
        private List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardDtoList;
        private List<EventsResponseDto.ReadCompanySummaryOneDto> companyEventDtoList;
        private List<VacationsResponseDto.ReadSummaryOneDto> personalVacationDtoList;
        private List<AttendancesResponseDto.ReadSummaryOneDto> personalAttendanceDtoList;
    }
}
