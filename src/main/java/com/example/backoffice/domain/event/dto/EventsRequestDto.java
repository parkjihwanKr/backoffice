package com.example.backoffice.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class EventsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCompanyEventsRequestDto{
        private String title;
        private String description;
        // front-end의 캘린더에서 startDate, endDate를 받는다고 가정
        // 해당 부분을 String으로 받는다고 함
        private String startDate;
        private String endDate;
        private MultipartFile file;
    }
}
