package com.example.backoffice.domain.mainPage.proxy;

import com.example.backoffice.domain.mainPage.converter.MainPageConverter;
import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;
import com.example.backoffice.domain.mainPage.service.MainPageService;
import com.example.backoffice.domain.member.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MainPageProxyService {

    private final MainPageService mainPageService;

    @Transactional(readOnly = true)
    public MainPageResponseDto.ReadOneDto read(Members loginMember) {
        MainPageResponseDto.SummaryExceptBoardDto summaryExceptBoardDtoList =
                mainPageService.readSummaryExceptBoard(loginMember);
        MainPageResponseDto.SummarizedBoardDto summaryBoardDtoList
                = mainPageService.readSummarizedBoard(loginMember);

        return MainPageConverter.toMainPageResponseDto(
                summaryExceptBoardDtoList.getPersonalFavoritesDtoList(),
                summaryBoardDtoList.getGeneralBoardDtoList(),
                summaryBoardDtoList.getDepartmentBoardDtoList(),
                summaryExceptBoardDtoList.getCompanyEventDtoList(),
                summaryExceptBoardDtoList.getPersonalVacationDtoList(),
                summaryExceptBoardDtoList.getPersonalAttendanceDtoList());
    }
}
