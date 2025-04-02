package com.example.backoffice.global.config;

import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.global.date.DateTimeUtils;
import com.example.backoffice.global.redis.deserializer.VacationPeriodDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule customModule = new SimpleModule();
        customModule.addDeserializer(
                VacationsResponseDto.ReadPeriodDto.class,
                new VacationPeriodDeserializer());
        objectMapper.registerModule(customModule);

        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        DateTimeFormatter dateTimeFormatter
                = DateTimeUtils.getCustomDateTimeFormatter();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(
                LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(
                LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {

            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

            builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
            builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
        };
    }
}
