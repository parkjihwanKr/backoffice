package com.example.backoffice.domain.event.exception.validator;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.exception.annotation.UrgentReasonRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UrgentReasonValidator implements ConstraintValidator<UrgentReasonRequired, EventsRequestDto.CreateVacationRequestDto> {

    @Override
    public boolean isValid(
            EventsRequestDto.CreateVacationRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getUrgent() != null && dto.getUrgent() && (dto.getReason() == null || dto.getReason().isEmpty())) {
            return false;
        }
        return true;
    }
}