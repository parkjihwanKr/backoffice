package com.example.backoffice.domain.mainPage.service;

import com.example.backoffice.domain.board.converter.BoardsConverter;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.service.BoardsServiceV1;
import com.example.backoffice.domain.event.service.EventsServiceV1;
import com.example.backoffice.domain.mainPage.converter.MainPageConverter;
import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final BoardsServiceV1 boardsService;
    private final EventsServiceV1 eventsService;
    private final VacationsServiceV1 vacationsService;

    @Transactional(readOnly = true)
    public MainPageResponseDto read(Members loginMember){
        List<Boards> generalBoardList
                = boardsService.findThreeByCreatedAtDesc(BoardType.GENERAL);
        List<BoardsResponseDto.ReadSummaryOneDto> generalBoardDtoList
                = BoardsConverter.toReadSummaryListDto(generalBoardList);
        List<Boards> departmentBoardList
                = boardsService.findThreeByCreatedAtDesc(BoardType.DEPARTMENT);
        List<BoardsResponseDto.ReadSummaryOneDto> departmentBoardDtoList
                = BoardsConverter.toReadSummaryListDto(departmentBoardList);
        return MainPageConverter.toMainPageResponseDto(
                generalBoardDtoList, departmentBoardDtoList, null, null, null);
    }
}
