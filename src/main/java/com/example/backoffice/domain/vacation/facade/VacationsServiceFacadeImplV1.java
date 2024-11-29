package com.example.backoffice.domain.vacation.facade;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
import com.example.backoffice.domain.vacation.converter.VacationsConverter;
import com.example.backoffice.domain.vacation.dto.VacationDateRangeDto;
import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.entity.*;
import com.example.backoffice.domain.vacation.exception.VacationsCustomException;
import com.example.backoffice.domain.vacation.exception.VacationsExceptionCode;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import com.example.backoffice.global.date.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VacationsServiceFacadeImplV1 implements VacationsServiceFacadeV1{
    private final MembersServiceV1 membersService;
    private final NotificationsServiceV1 notificationsService;
    private final VacationsServiceV1 vacationsService;
    private final VacationPeriodHolder vacationPeriodHolder;

    // 휴가 관련 피드백 : 생성할 때, 승인이 될 시에, 잔여 휴가 일 변경
    // 날짜가 시작 날이 토요일/ 일요일은 안됨, 마지막일 또한 토요일/ 일요일은 안됨.
    @Override
    public VacationsResponseDto.UpdatePeriodDto updatePeriod(
            Members loginMember, VacationsRequestDto.UpdatePeriodDto requestDto){
        validateUpdatePermission(loginMember);

        LocalDateTime newStartDate = DateTimeUtils.parse(requestDto.getStartDate());
        LocalDateTime newEndDate = DateTimeUtils.parse(requestDto.getEndDate());

        // 현재 월을 기준으로 검증
        LocalDateTime now = DateTimeUtils.getCurrentDateTime();
        if (newStartDate.getMonth() != now.getMonth() || newEndDate.getMonth() != now.getMonth()) {
            throw new VacationsCustomException(VacationsExceptionCode.INVALID_VACATION_PERIOD);
        }

        LocalDateTime tomorrow = DateTimeUtils.getTomorrow();
        if(newStartDate.isBefore(tomorrow)){
            throw new VacationsCustomException(VacationsExceptionCode.INVALID_START_DATE);
        }

        if(newStartDate.isAfter(newEndDate)){
            throw new VacationsCustomException(VacationsExceptionCode.END_DATE_BEFORE_START_DATE);
        }

        vacationPeriodHolder.setVacationPeriod(newStartDate, newEndDate);

        List<Members> memberList = membersService.findAll();

        for(Members member : memberList){
            notificationsService.generateEntityAndSendMessage(
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

        // 1. 기간 범위 검증
        VacationDateRangeDto vacationDateRangeDto = validateVacationDate(
                null, loginMember, requestDto.getStartDate(), requestDto.getEndDate(),
                requestDto.getUrgent());

        VacationType vacationType = VacationsConverter.toVacationType(requestDto.getVacationType());
        validateVacationType(vacationType, requestDto.getUrgent());

        Vacations vacation = VacationsConverter.toEntity(
                requestDto.getTitle(), requestDto.getUrgentReason(),
                vacationDateRangeDto, vacationType,
                requestDto.getUrgent(), loginMember);

        vacationsService.save(vacation);

        if(requestDto.getUrgent()){
            sendUrgentOneForHRManager(loginMember);
            if(vacationType.equals(VacationType.ANNUAL_LEAVE)){
                throw new VacationsCustomException(VacationsExceptionCode.DO_NOT_NEED_URGENT);
            }
        }

        return VacationsConverter.toCreateOneDto(vacation);
    }

    @Override
    @Transactional(readOnly = true)
    public VacationsResponseDto.ReadDayDto readDay(Long vacationId, Members loginMember) {
        Vacations vacation = vacationsService.findById(vacationId);
        if (vacation.getOnVacationMember().getId().equals(loginMember.getId())) {
            throw new VacationsCustomException(VacationsExceptionCode.NO_PERMISSION_TO_READ_VACATION);
        }
        return VacationsConverter.toReadDayDto(vacation);
    }

    @Override
    @Transactional
    public VacationsResponseDto.UpdateOneDto updateOne(
            Long vacationId, Members loginMember, VacationsRequestDto.UpdateOneDto requestDto) {

        Vacations vacation = vacationsService.findById(vacationId);

        VacationType vacationType = VacationsConverter.toVacationType(requestDto.getVacationType());
        validateVacationType(vacationType, requestDto.getUrgent());

        // 해당 이벤트의 주인이 로그인한 사람인지?
        validateMemberPermission(
                loginMember, vacation.getOnVacationMember().getId(),
                null, VacationCrudType.UPDATE_VACATION);

        VacationDateRangeDto vacationDateRangeDto = validateVacationDate(
                vacationId, loginMember, requestDto.getStartDate(),
                requestDto.getEndDate(), requestDto.getUrgent());

        if(requestDto.getUrgent()){
            sendUrgentOneForHRManager(loginMember);
            if(vacationType.equals(VacationType.ANNUAL_LEAVE)){
                throw new VacationsCustomException(VacationsExceptionCode.DO_NOT_NEED_URGENT);
            }
        }

        vacation.update(requestDto.getTitle(), requestDto.getUrgentReason(),
                vacationDateRangeDto.getDateRange().getStartDate(),
                vacationDateRangeDto.getDateRange().getEndDate(), vacationType,
                false);

        return VacationsConverter.toUpdateOneDto(vacation);
    }

    // 승인 요청만 받는 형태이기에 Admin의 RequestDto를 받을 필요가 없다.
    @Override
    @Transactional
    public VacationsResponseDto.UpdateOneForAdminDto updateOneForAdmin(
            Long vacationId, Members loginMember){
        // 1. 로그인 멤버가 CEO || HR_MANAGER인지 확인
        validateUpdatePermission(loginMember);

        // 2. 바꾸려는 휴가가 있는지?
        Vacations vacation = vacationsService.findById(vacationId);

        // 3. isAccepted 변경
        Boolean initialIsAccepted = vacation.getIsAccepted();
        vacation.updateIsAccepted(!initialIsAccepted);

        // 4. 알림 메시지를 isAccepted 상태에 따라 변경
        String notificationMessage = initialIsAccepted
                ? vacation.getOnVacationMember().getMemberName() + "님의 휴가가 승인되었습니다."
                : vacation.getOnVacationMember().getMemberName() + "님의 휴가가 미승인되었습니다.";

        notificationsService.generateEntityAndSendMessage(
                NotificationsConverter.toNotificationData(
                        loginMember, vacation.getOnVacationMember(),
                        null, null, null, null,
                        notificationMessage),
                NotificationType.IS_ACCEPTED_VACATION);

        // 5. 휴가 가는 사람의 잔여 휴가 일 수 차감
        LocalDateTime currentDate = vacation.getStartDate();
        int vacationDays = 0;

        // 시작일부터 종료일까지 모든 날짜 확인
        while (!currentDate.isAfter(vacation.getEndDate())) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            // 토요일이나 일요일이 아니면 휴가 일수에 추가
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                vacationDays++;
            }

            // 다음 날짜로 이동
            currentDate = currentDate.plusDays(1);
        }

        // 휴가가 승인되면 잔여 휴가 일 수에서 차감, 미승인되면 다시 증가
        if (!initialIsAccepted) {
            // 승인된 경우, 잔여 휴가 일 수에서 차감
            membersService.minusVacationDays(vacation.getOnVacationMember(), vacationDays);
        } else {
            // 미승인된 경우, 휴가 일 수 복원 (즉, 다시 추가)
            // 휴가 db에서 삭제
            membersService.addVacationDays(vacation.getOnVacationMember(), vacationDays);
            vacationsService.deleteById(vacationId);
        }

        // 6. DTO 전송
        return VacationsConverter.toUpdateOneForAdminDto(vacation);
    }

    @Override
    @Transactional
    public void deleteOne(Long vacationId, Members loginMember) {
        Vacations vacation = vacationsService.findById(vacationId);
        validateMemberPermission(
                loginMember, vacation.getOnVacationMember().getId(),
                null, VacationCrudType.DELETE_VACATION);

        vacationsService.deleteById(vacationId);
    }

    // 필터링된 휴가 관리 시스템 입장 시, 보이는 휴가 리스트
    @Override
    @Transactional(readOnly = true)
    public List<VacationsResponseDto.ReadMonthDto> readForHrManager(
            Long year, Long month, Boolean isAccepted, Boolean urgent,
            String department, Members loginMember){

        // 1. 휴가 관리 시스템을 읽을 권한이 있는지?
        membersService.findHRManagerOrCEO(loginMember);

        // 2. 시작일과 마감일을 DateTimeUtils에서 가져오기
        LocalDateTime startDate = DateTimeUtils.getStartDayOfMonth(year, month);
        LocalDateTime endDate = DateTimeUtils.getEndDayOfMonth(year, month); // 다음 달 1일 바로 전

        // 3. 초기 상태와 필터링 상태에 따른 리턴 형태 변경
        MemberDepartment memberDepartment = null;

        // 3-1. 초기 상태 리턴
        if (department == null && urgent == null && isAccepted == null){
            List<Vacations> vacationList
                    = vacationsService.findVacationsOnMonth(startDate, endDate);
            return VacationsConverter.toReadMonthForHrManager(vacationList);
        }

        // 3-2 department가 null이 아닐 때 필터링 상태 리턴
        if(department != null){
            memberDepartment = membersService.findDepartment(department);
        }

        // 3-3 department가 null이면 모든 부서의 값을 가져온 상태로 리턴
        List<Vacations> vacationList
                = vacationsService.findFilteredVacationsOnMonth(
                        startDate, endDate, isAccepted, urgent, memberDepartment);

        return VacationsConverter.toReadMonthForHrManager(vacationList);
    }

    @Override
    @Transactional
    public void deleteOneForHrManager(
            Long vacationId, VacationsRequestDto.DeleteOneForAdminDto requestDto,
            Members loginMember){
        membersService.findHRManagerOrCEO(loginMember);
        Vacations vacation = vacationsService.findById(vacationId);
        notificationsService.generateEntityAndSendMessage(
                NotificationsConverter.toNotificationData(
                        loginMember, vacation.getOnVacationMember(),
                        null, null, null, null,
                        requestDto.getReason()),
                NotificationType.DELETE_VACATION_FOR_ADMIN);

        vacationsService.deleteById(vacationId);
    }
    private VacationDateRangeDto validateVacationDate(
            Long vacationId, Members loginMember, String startDate, String endDate, Boolean urgent) {

        LocalDateTime now = DateTimeUtils.getCurrentDateTime();
        LocalDateTime startVacationDate = DateTimeUtils.parse(startDate);
        LocalDateTime endVacationDate = DateTimeUtils.parse(endDate);

        // 1. 휴가 시작일과 종료일의 유효성 확인 (시작일이 종료일보다 빠른지)
        if (!startVacationDate.isBefore(endVacationDate)) {
            throw new VacationsCustomException(VacationsExceptionCode.END_DATE_BEFORE_START_DATE);
        }
        if (startVacationDate.isBefore(now)) {
            throw new VacationsCustomException(VacationsExceptionCode.INVALID_START_DATE);
        }
        // 휴가 시작일이 토요일/일요일이면 안되고 종료일 또한 토요일/일요일이면 안됨.
        if (startVacationDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                startVacationDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new VacationsCustomException(VacationsExceptionCode.INVALID_START_DATE_WEEKEND);
        }

        if (endVacationDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                endVacationDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new VacationsCustomException(VacationsExceptionCode.INVALID_END_DATE_WEEKEND);
        }

        // 2. 잔여 휴가 일수 확인
        long vacationDays = Duration.between(startVacationDate, endVacationDate).toDays();
        if (loginMember.getRemainingVacationDays() < vacationDays) {
            throw new VacationsCustomException(VacationsExceptionCode.INSUFFICIENT_VACATION_DAYS);
        }

        // 3. 휴가 기간이 다른 휴가와 겹치는지 확인
        validateVacationOverlap(vacationId, loginMember.getId(), startVacationDate, endVacationDate);

        // 4. 긴급 요청 검증
        if(!urgent){
            validateVacationRateLimit(startVacationDate, vacationDays);
        }else{
            // 긴급한 요청이면 밑의 휴가 제한, 신청 기간 검증, 다음 달의 휴가 신청 가능한 로직 무시
            return VacationsConverter.toVacationDateRangeDto(startVacationDate, endVacationDate);
        }

        // 5. 30일 이상 휴가 제한
        if (vacationDays >= 30) {
            throw new VacationsCustomException(VacationsExceptionCode.INVALID_VACATION_DAYS);
        }

        // 6. 휴가 신청 가능 기간 검증 (validateVacationRequestPeriod 내용 통합)
        VacationPeriod allowedVacationPeriod = vacationPeriodHolder.getVacationPeriod();
        if (!allowedVacationPeriod.isWithinAllowedPeriod(now)) {
            throw new VacationsCustomException(VacationsExceptionCode.OUT_OF_VACATION_REQUEST_PERIOD);
        }

        // 7. 다음 달의 휴가만 신청 가능
        LocalDateTime nextMonthStart
                = LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime nextMonthEnd
                = nextMonthStart.withDayOfMonth(30);
        if (startVacationDate.isBefore(nextMonthStart) || startVacationDate.isAfter(nextMonthEnd)) {
            throw new VacationsCustomException(VacationsExceptionCode.RESTRICTED_DATE_RANGE);
        }

        return VacationsConverter.toVacationDateRangeDto(startVacationDate, endVacationDate);
    }

    // 긴급하지 않을 때 휴가율을 따로 두어 제한
    private void validateVacationRateLimit(LocalDateTime startDate, long vacationDays) {
        Long memberTotalCount = membersService.findMemberTotalCount();

        // 1. 모든 멤버의 수에 비례에서 휴가 사용
        if (memberTotalCount > 10) {
            for (long i = 0; i < vacationDays; i++) {
                LocalDateTime customStartDate = startDate.plusDays(i);
                long vacationingMembersCount = vacationsService.countVacationingMembers(customStartDate);

                double vacationRate = (double) vacationingMembersCount / memberTotalCount;

                // 비율이 0.3이상이면 휴가 사용이 안됨.
                if (vacationRate > 0.3) {
                    throw new VacationsCustomException(VacationsExceptionCode.EXCEEDS_VACATION_RATE_LIMIT);
                }
            }
        }

        // 2. 긴급하지 않을 때, 신청 날짜에 맞춰서 신청해야하는 로직 추가
        LocalDateTime nextMonth = null;
        if(vacationPeriodHolder.getVacationPeriod().getStartDate().getMonthValue() == 12){
            nextMonth = LocalDateTime.of(
                    vacationPeriodHolder.getVacationPeriod().getStartDate().getYear() + 1, 1,
                    1, 0, 0, 0);
        }else{
            nextMonth = LocalDateTime.of(
                    vacationPeriodHolder.getVacationPeriod().getStartDate().getYear(),
                    vacationPeriodHolder.getVacationPeriod().getStartDate().getMonthValue() + 1,
                    1, 0, 0, 0);
        }
        LocalDateTime nextMonthStart = nextMonth;
        LocalDateTime nextMonthEnd = nextMonth.plusMonths(1).minusSeconds(1);

        if (startDate.isBefore(nextMonthStart) || startDate.isAfter(nextMonthEnd)) {
            throw new VacationsCustomException(VacationsExceptionCode.OUT_OF_VACATION_REQUEST_PERIOD);
        }
    }

    private void sendUrgentOneForHRManager(Members loginMember) {
        Members admin = membersService.findHRManagerOrCEO(loginMember);

        notificationsService.generateEntityAndSendMessage(
                NotificationsConverter.toNotificationData(
                        admin, loginMember, null, null, null, null, null),
                NotificationType.URGENT_VACATION);
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

    public void validateUpdatePermission(Members loginMember){
        if(!loginMember.getPosition().equals(MemberPosition.CEO)
                && !(loginMember.getDepartment().equals(MemberDepartment.HR)
                && loginMember.getPosition().equals(MemberPosition.MANAGER))){
            throw new VacationsCustomException(VacationsExceptionCode.NO_PERMISSION_TO_UPDATE_VACATION);
        }
    }

    private void validateVacationOverlap(Long vacationId, Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        boolean isOverlapping;

        if(vacationId == null){
            // Create type
            isOverlapping = vacationsService.existsVacationForMemberInDateRange(null, memberId, startDate, endDate);
        }else{
            // Update type
            // 현재 변경하려는 휴가를 제외한 멤버 아이디의 휴가를 탐색해서 해당 날짜가 겹치는지 아닌지 확인
            isOverlapping = vacationsService.existsVacationForMemberInDateRange(vacationId, memberId, startDate, endDate);
        }

        if (isOverlapping) {
            throw new VacationsCustomException(VacationsExceptionCode.OVERLAPPING_VACATION_DATES);
        }
    }

    private void validateVacationType(VacationType vacationType, Boolean urgent){
        switch(vacationType) {
            case ANNUAL_LEAVE -> {
                if(urgent){
                    throw new VacationsCustomException(VacationsExceptionCode.DO_NOT_NEED_URGENT);
                }
            }
            case SICK_LEAVE , URGENT_LEAVE -> {
                if(!urgent){
                    throw new VacationsCustomException(VacationsExceptionCode.NEED_URGENT);
                }
            }
        }
    }
}
