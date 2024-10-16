package com.example.backoffice.domain.vacation.converter;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.dto.VacationDateRangeDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.entity.VacationPeriod;
import com.example.backoffice.domain.vacation.entity.VacationType;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.example.backoffice.domain.vacation.exception.VacationsCustomException;
import com.example.backoffice.domain.vacation.exception.VacationsExceptionCode;
import com.example.backoffice.global.common.DateRange;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class VacationsConverter {

    public static VacationDateRangeDto toVacationDateRangeDto(
            LocalDateTime startDate, LocalDateTime endDate){
        return VacationDateRangeDto.builder()
                .dateRange(
                        DateRange.builder()
                                .startDate(startDate)
                                .endDate(endDate).build()).build();
    }

    public static VacationType toVacationType(String vacationType){
        return switch (vacationType) {
            case "연가" -> VacationType.ANNUAL_LEAVE;
            case "병가" -> VacationType.SICK_LEAVE;
            case "긴급한 휴가" -> VacationType.URGENT_LEAVE;
            default -> throw new VacationsCustomException(VacationsExceptionCode.NOT_FOUND_VACATION_TYPE);
        };
    }

    public static Boolean toIsAccepted(String isAcceptedType){
        return switch (isAcceptedType) {
            case "all" -> null;
            case "false" -> false;
            case "true" -> true;
            default -> throw new VacationsCustomException(
                    VacationsExceptionCode.NOT_FOUND_VACATION_IS_ACCEPTED_TYPE);
        };
    }

    public static Vacations toEntity(
            String title, String reason, VacationDateRangeDto vacationDateRangeDto,
            VacationType vacationType ,Boolean urgent, Members loginMember){
        return Vacations.builder()
                .title(title)
                .urgentReason(reason)
                .vacationType(vacationType)
                .urgent(urgent)
                .onVacationMember(loginMember)
                .isAccepted(false)
                .startDate(vacationDateRangeDto.getDateRange().getStartDate())
                .endDate(vacationDateRangeDto.getDateRange().getEndDate())
                .build();
    }

    public static VacationsResponseDto.UpdatePeriodDto toUpdatePeriodDto(
            LocalDateTime newStartDate, LocalDateTime newEndDate){
        return VacationsResponseDto.UpdatePeriodDto.builder()
                .startDate(newStartDate)
                .endDate(newEndDate)
                .build();
    }

    public static VacationsResponseDto.CreateOneDto toCreateOneDto(
            Vacations vacation){
        return VacationsResponseDto.CreateOneDto.builder()
                .vacationType(vacation.getVacationType())
                .title(vacation.getTitle())
                .urgentReason(vacation.getUrgentReason())
                .startDate(vacation.getStartDate())
                .endDate(vacation.getEndDate())
                .build();
    }

    public static VacationsResponseDto.ReadDayDto toReadDayDto(Vacations vacation){
        return VacationsResponseDto.ReadDayDto.builder()
                .vacationId(vacation.getId())
                .onVacationMemberName(vacation.getOnVacationMember().getMemberName())
                .vacationType(vacation.getVacationType())
                .isAccepted(vacation.getIsAccepted())
                .title(vacation.getTitle())
                .urgentReason(vacation.getUrgentReason())
                .startDate(vacation.getStartDate())
                .endDate(vacation.getEndDate())
                .build();
    }

    public static List<VacationsResponseDto.ReadDayDto> toReadDayDtoList(List<Vacations> vacationList){
        return vacationList.stream()
                .map(VacationsConverter::toReadDayDto)
                .collect(Collectors.toList());
    }

    public static List<VacationsResponseDto.ReadMonthDto> toReadMonthDtoList(List<Vacations> vacationList){
        return vacationList.stream()
                .map(vacation ->
                        VacationsResponseDto.ReadMonthDto.builder()
                                .vacationId(vacation.getId())
                                .onVacationMemberName(vacation.getOnVacationMember().getMemberName())
                                .title(vacation.getTitle())
                                .urgentReason(vacation.getUrgentReason())
                                .startDate(vacation.getStartDate())
                                .endDate(vacation.getEndDate())
                                .build()
                ).collect(Collectors.toList());
    }

    public static VacationsResponseDto.UpdateOneDto toUpdateOneDto(Vacations vacation){
        return VacationsResponseDto.UpdateOneDto.builder()
                .vacationId(vacation.getId())
                .vacationType(vacation.getVacationType())
                .title(vacation.getTitle())
                .urgentReason(vacation.getUrgentReason())
                .startDate(vacation.getStartDate())
                .endDate(vacation.getEndDate())
                .build();
    }

    public static VacationsResponseDto.UpdateOneForAdminDto toUpdateOneForAdminDto(
            Vacations vacation){
        return VacationsResponseDto.UpdateOneForAdminDto.builder()
                .acceptedVacationMemberName(vacation.getOnVacationMember().getMemberName())
                .vacationId(vacation.getId())
                .isAccepted(vacation.getIsAccepted())
                .build();
    }

    public static List<VacationsResponseDto.ReadOneIsAcceptedDto> toReadOneIsAcceptedDto(
            List<Vacations> isAcceptedVacationList){
        return isAcceptedVacationList.stream().map(
                vacation -> VacationsResponseDto.ReadOneIsAcceptedDto.builder()
                        .vacationId(vacation.getId())
                        .onVacationMemberName(vacation.getOnVacationMember().getMemberName())
                        .startDate(vacation.getStartDate())
                        .endDate(vacation.getEndDate())
                        .isAccepted(vacation.getIsAccepted())
                        .build()
        ).collect(Collectors.toList());
    }

    public static List<VacationsResponseDto.ReadMonthDto> toReadMonthForHrManager(
            List<Vacations> vacationList){
        return vacationList.stream().map(
            vacation -> VacationsResponseDto.ReadMonthDto.builder()
                    .vacationId(vacation.getId())
                    .vacationType(vacation.getVacationType())
                    .department(vacation.getOnVacationMember().getDepartment())
                    .onVacationMemberName(vacation.getOnVacationMember().getMemberName())
                    .title(vacation.getTitle())
                    .isAccepted(vacation.getIsAccepted())
                    .urgentReason(vacation.getUrgentReason())
                    .startDate(vacation.getStartDate())
                    .endDate(vacation.getEndDate())
                    .createdAt(vacation.getCreatedAt())
                    .modifiedAt(vacation.getModifiedAt())
                    .build()
        ).collect(Collectors.toList());
    }
}
