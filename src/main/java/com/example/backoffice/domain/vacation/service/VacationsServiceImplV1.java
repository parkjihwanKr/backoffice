package com.example.backoffice.domain.vacation.service;

import com.example.backoffice.domain.member.converter.MembersConverter;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import com.example.backoffice.domain.vacation.converter.VacationsConverter;
import com.example.backoffice.domain.vacation.dto.VacationDateRangeDto;
import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.entity.*;
import com.example.backoffice.domain.vacation.exception.VacationsCustomException;
import com.example.backoffice.domain.vacation.exception.VacationsExceptionCode;
import com.example.backoffice.domain.vacation.repository.VacationsRepository;
import com.example.backoffice.global.common.DateRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.backoffice.global.common.DateTimeFormatters.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class VacationsServiceImplV1 implements VacationsServiceV1 {

    private final MembersServiceV1 membersService;
    private final NotificationsServiceFacadeV1 notificationsServiceFacade;
    private final VacationsRepository vacationsRepository;
    private final VacationPeriodHolder vacationPeriodHolder;

    @Override
    public VacationsResponseDto.UpdatePeriodDto updatePeriod(
            Members loginMember, VacationsRequestDto.UpdatePeriodDto requestDto){
        Members hrManager = membersService.findHRManager();

        if (!hrManager.getId().equals(loginMember.getId())
                && !loginMember.getPosition().equals(MemberPosition.CEO)) {
            throw new VacationsCustomException(VacationsExceptionCode.NO_PERMISSION_TO_UPDATE_VACATION);
        }

        LocalDateTime newStartDate = LocalDateTime.parse(requestDto.getStartDate(), DATE_TIME_FORMATTER);
        LocalDateTime newEndDate = LocalDateTime.parse(requestDto.getEndDate(), DATE_TIME_FORMATTER);

        vacationPeriodHolder.setVacationPeriod(
                VacationPeriod.builder()
                        .startDate(newStartDate)
                        .endDate(newEndDate)
                        .build());

        List<Members> memberList = membersService.findAll();

        for(Members member : memberList){
            notificationsServiceFacade.createOne(
                    NotificationsConverter.toNotificationData(
                            member, loginMember, null, null, null, null,
                                    "변경된 휴가 신청 기간 안내입니다 : "
                                    +vacationPeriodHolder.getVacationPeriod().getStartDate() + " ~ "
                                    +vacationPeriodHolder.getVacationPeriod().getEndDate()),
                    NotificationType.UPDATE_VACATION_PERIOD);
        }

        return VacationsConverter.toUpdatePeriodDto(newStartDate, newEndDate);
    }

    @Override
    @Transactional
    public VacationsResponseDto.CreateOneDto createOne(
            Members loginMember, VacationsRequestDto.CreateOneDto requestDto) {

        VacationDateRangeDto vacationDateRangeDto = validateVacationDate(
                loginMember, requestDto.getStartDate(), requestDto.getEndDate(),
                requestDto.getUrgent());

        validateMemberOnVacation(loginMember.getId());

        Vacations vacation = VacationsConverter.toEntity(
                requestDto.getTitle(), requestDto.getUrgentReason(), vacationDateRangeDto, loginMember);

        sendUrgentOneForHRManager(requestDto.getUrgent(), loginMember);

        vacationsRepository.save(vacation);

        return VacationsConverter.toCreateOneDto(vacation);
    }

    @Override
    @Transactional(readOnly = true)
    public VacationsResponseDto.ReadDayDto readDay(Long vacationId, Members loginMember) {
        Vacations vacation = findById(vacationId);
        if (vacation.getOnVacationMember().getId().equals(loginMember.getId())) {
            throw new VacationsCustomException(VacationsExceptionCode.NO_PERMISSION_TO_READ_VACATION);
        }
        return VacationsConverter.toReadDayDto(vacation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationsResponseDto.ReadDayDto> readDayForAdmin(
            String department, Long year, Long month, Long day, Members loginMember) {
        // 1. 읽을 권한이 있는지 확인
        MemberDepartment onVacationMemberDepartment = MembersConverter.toDepartment(department);
        validateMemberPermission(loginMember, null, onVacationMemberDepartment, VacationCrudType.READ_VACATION);

        // 2. startDate, endDate 설정
        LocalDateTime startDate = LocalDateTime.of(year.intValue(), month.intValue(), day.intValue(), 0, 0);

        // 3. 휴가 중인 멤버 리스트 조회
        List<Vacations> vacationList = vacationsRepository.findVacationsOnDate(startDate);

        // 4. 결과를 DTO로 변환하여 반환
        return VacationsConverter.toReadDayDtoList(vacationList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationsResponseDto.ReadMonthDto> readMonthForAdmin(
            String department, Long year, Long month, Members loginMember) {
        // 1. 권한 검증
        MemberDepartment onVacationMemberDepartment = MembersConverter.toDepartment(department);
        validateMemberPermission(
                loginMember, null,
                onVacationMemberDepartment, VacationCrudType.READ_VACATION);

        // 2. 해당 년, 월의 시작 날짜와 끝 날짜 설정
        LocalDateTime startDate = LocalDateTime.of(year.intValue(), month.intValue(), 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1); // 다음 달 1일 바로 전

        List<Vacations> vacationList = vacationsRepository.findVacationsOnMonth(startDate, endDate);

        // 4. 결과를 DTO로 변환하여 반환
        return VacationsConverter.toReadMonthDtoList(vacationList);
    }


    @Override
    @Transactional
    public VacationsResponseDto.UpdateOneDto updateOne(
            Long vacationId, Members loginMember, VacationsRequestDto.UpdateOneDto requestDto) {

        Vacations vacation = findById(vacationId);
        validateMemberOnVacation(loginMember.getId());

        // 해당 이벤트의 주인이 로그인한 사람인지?
        validateMemberPermission(
                loginMember, vacation.getOnVacationMember().getId(),
                null, VacationCrudType.UPDATE_VACATION);

        VacationDateRangeDto vacationDateRangeDto = validateVacationDate(
                loginMember, requestDto.getStartDate(),
                requestDto.getEndDate(), requestDto.getUrgent());

        VacationType vacationType = VacationsConverter.toVacationType(requestDto.getVacationType());

        sendUrgentOneForHRManager(requestDto.getUrgent(), loginMember);

        String vacationTitle = loginMember.getName() + "님의 휴가 계획";
        vacation.update(vacationTitle, requestDto.getUrgentReason(),
                vacationDateRangeDto.getDateRange().getStartDate(),
                vacationDateRangeDto.getDateRange().getEndDate(), vacationType);

        return VacationsConverter.toUpdateOneDto(vacation);
    }

    @Override
    @Transactional
    public void deleteOne(Long vacationId, Members loginMember) {
        Vacations vacation = findById(vacationId);
        validateMemberPermission(
                loginMember, vacation.getOnVacationMember().getId(),
                null, VacationCrudType.DELETE_VACATION);

        vacationsRepository.deleteById(vacationId);
    }

    private void validateMemberOnVacation(Long loginMemberId) {
        Boolean isOnVacation = vacationsRepository.existsByOnVacationMemberId(loginMemberId);

        if (isOnVacation) {
            throw new VacationsCustomException(VacationsExceptionCode.NO_PERMISSION_TO_CREATE_VACATION);
        }
    }

    private VacationDateRangeDto validateVacationDate(
            Members loginMember, String startDate, String endDate, Boolean urgent) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startVacationDate = LocalDateTime.parse(startDate, DATE_TIME_FORMATTER);
        LocalDateTime endVacationDate = LocalDateTime.parse(endDate, DATE_TIME_FORMATTER);

        validateVacationRequestPeriod(VacationsConverter.toVacationDateRangeDto(startVacationDate, endVacationDate), urgent);

        long vacationDays = Duration.between(startVacationDate, endVacationDate).toDays();
        if (loginMember.getRemainingVacationDays() < vacationDays) {
            throw new VacationsCustomException(VacationsExceptionCode.INSUFFICIENT_VACATION_DAYS);
        }

        if (vacationDays >= 30) {
            throw new VacationsCustomException(VacationsExceptionCode.INVALID_VACATION_DAYS);
        }

        if (!startVacationDate.isBefore(endVacationDate)) {
            throw new VacationsCustomException(VacationsExceptionCode.END_DATE_BEFORE_START_DATE);
        }

        if (!urgent) {
            validateVacationRateLimit(startVacationDate, vacationDays);
        }

        return VacationsConverter.toVacationDateRangeDto(startVacationDate, endVacationDate);
    }

    private void validateVacationRateLimit(LocalDateTime startDate, long vacationDays) {
        Long memberTotalCount = membersService.findMemberTotalCount();

        if (memberTotalCount > 10) {
            for (long i = 0; i < vacationDays; i++) {
                LocalDateTime customStartDate = startDate.plusDays(i);
                long vacationingMembersCount = vacationsRepository.countVacationingMembers(customStartDate);

                double vacationRate = (double) vacationingMembersCount / memberTotalCount;

                if (vacationRate > 0.3) {
                    throw new VacationsCustomException(VacationsExceptionCode.EXCEEDS_VACATION_RATE_LIMIT);
                }
            }
        }
    }

    private void validateVacationRequestPeriod(VacationDateRangeDto vacationDateRangeDto, Boolean urgent) {
        LocalDateTime startDate = vacationDateRangeDto.getDateRange().getStartDate();
        LocalDateTime endDate = vacationDateRangeDto.getDateRange().getEndDate();
        LocalDateTime now = LocalDateTime.now();

        if (urgent) {
            if (startDate.isBefore(now)) {
                throw new VacationsCustomException(VacationsExceptionCode.INVALID_START_DATE);
            }
            return;
        }

        VacationPeriod allowedVacationPeriod = vacationPeriodHolder.getVacationPeriod();

        if (!allowedVacationPeriod.isWithinAllowedPeriod(startDate, endDate)) {
            throw new VacationsCustomException(VacationsExceptionCode.OUT_OF_VACATION_REQUEST_PERIOD);
        }

        // 다음 달의 휴가만 신청 가능
        LocalDateTime nextMonthStart = now.plusMonths(1).withDayOfMonth(1);
        LocalDateTime nextMonthEnd = nextMonthStart.withDayOfMonth(30);
        if (startDate.isBefore(nextMonthStart) || startDate.isAfter(nextMonthEnd)) {
            throw new VacationsCustomException(VacationsExceptionCode.RESTRICTED_DATE_RANGE);
        }
    }

    private void sendUrgentOneForHRManager(Boolean urgent, Members loginMember) {
        if (urgent) {
            Members hrManager = membersService.findHRManager();
            notificationsServiceFacade.createOne(
                    NotificationsConverter.toNotificationData(
                            hrManager, loginMember, null, null, null, null, null),
                    NotificationType.URGENT_VACATION);
        }
    }

    private void validateMemberPermission(
            Members loginMember, Long onVacationMemberId,
            MemberDepartment onVacationMemberDepartment, VacationCrudType vacationCrudType) {
        switch (vacationCrudType) {
            case READ_VACATION: {
                if (!(loginMember.getPosition().equals(MemberPosition.CEO)
                        || (loginMember.getPosition().equals(MemberPosition.MANAGER)
                        && loginMember.getDepartment().equals(onVacationMemberDepartment)))) {
                    throw new VacationsCustomException(VacationsExceptionCode.NO_PERMISSION_TO_READ_VACATION);
                }
                break;
            }
            case UPDATE_VACATION: {
                if (!loginMember.getId().equals(onVacationMemberId)) {
                    throw new VacationsCustomException(VacationsExceptionCode.NO_PERMISSION_TO_UPDATE_VACATION);
                }
                break;
            }
            case DELETE_VACATION: {
                if (!loginMember.getId().equals(onVacationMemberId)
                        && !(loginMember.getPosition().equals(MemberPosition.MANAGER)
                        && loginMember.getDepartment().equals(MemberDepartment.HR))) {
                    throw new VacationsCustomException(VacationsExceptionCode.NO_PERMISSION_TO_DELETE_VACATION);
                }
                break;
            }
            default:
                throw new VacationsCustomException(VacationsExceptionCode.NOT_FOUND_VACATION_TYPE);
        }
    }

    private Vacations findById(Long vacationId) {
        return vacationsRepository.findById(vacationId).orElseThrow(
                () -> new VacationsCustomException(VacationsExceptionCode.NOT_FOUND_VACATIONS));
    }
}
