package com.example.backoffice.global.redis.deserializer;

import com.example.backoffice.domain.vacation.converter.VacationsConverter;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.global.date.DateTimeUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.time.LocalDateTime;

public class VacationPeriodDeserializer extends JsonDeserializer<VacationsResponseDto.ReadPeriodDto> {

    @Override
    public VacationsResponseDto.ReadPeriodDto deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        ObjectNode node = parser.getCodec().readTree(parser);

        // JSON에서 startDate 및 endDate 추출
        String startDateString = node.get("startDate").asText();
        String endDateString = node.get("endDate").asText();

        // LocalDateTime 변환
        LocalDateTime startDate = DateTimeUtils.parse(startDateString);
        LocalDateTime endDate = DateTimeUtils.parse(endDateString);

        // ReadPeriodDto 생성 및 값 설정
        return VacationsConverter.toReadPeriodDto(startDate, endDate);
    }
}
