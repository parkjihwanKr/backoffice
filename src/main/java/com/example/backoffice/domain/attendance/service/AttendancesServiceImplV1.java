package com.example.backoffice.domain.attendance.service;

import com.example.backoffice.domain.attendance.converter.AttendancesConverter;
import com.example.backoffice.domain.attendance.dto.AttendancesRequestDto;
import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.attendance.exception.AttendancesCustomException;
import com.example.backoffice.domain.attendance.exception.AttendancesExceptionCode;
import com.example.backoffice.domain.attendance.repository.AttendancesRepository;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import com.example.backoffice.global.common.DateRange;
import com.example.backoffice.global.date.DateTimeUtils;
import com.example.backoffice.global.redis.UpcomingAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendancesServiceImplV1 implements AttendancesServiceV1{

    private final MembersServiceV1 membersService;
    private final NotificationsServiceV1 notificationsService;
    private final VacationsServiceV1 vacationsService;
    private final AttendancesRepository attendancesRepository;
    private final UpcomingAttendanceRepository upcomingAttendanceRepository;

    @Override
    @Transactional
    public void create(Boolean isWeekDay){
        List<Members> memberList = membersService.findAll();
        List<Attendances> attendancesList = new ArrayList<>();

        for (Members member : memberList) {
            // Redis에서 해당 멤버의 DateRange 데이터 가져오기
            DateRange cachedDateRange
                    = upcomingAttendanceRepository.getValue(
                            member.getId(), DateRange.class);

            if (cachedDateRange != null && DateTimeUtils.isInDateRange(cachedDateRange)) {
                // Redis에 저장된 기간 내라면 외근으로 처리
                attendancesList.add(
                        AttendancesConverter.toEntity(
                                member, AttendanceStatus.OUT_OF_OFFICE,
                                DateTimeUtils.getTodayCheckInTime(), DateTimeUtils.getTodayCheckOutTime()));
            } else {
                // 멤버가 휴가 중일 때
                if(member.getOnVacation()){
                    attendancesList.add(
                            AttendancesConverter.toEntity(
                                    member, AttendanceStatus.VACATION));
                }
                // 주일인 경우
                if(isWeekDay){
                    attendancesList.add(
                            AttendancesConverter.toEntity(
                                    member, AttendanceStatus.ABSENT));
                // 주말인 경우
                }else{
                    attendancesList.add(
                            AttendancesConverter.toEntity(
                                    member, AttendanceStatus.HOLIDAY));
                }
            }
        }
        attendancesRepository.saveAll(attendancesList);
    }

    @Override
    @Transactional
    public AttendancesResponseDto.UpdateCheckInTimeDto updateCheckInTimeForMember(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckInTimeDto requestDto,
            Members loginMember){
        Attendances attendance = findById(attendanceId);
        membersService.matchLoginMember(
                attendance.getMember(), loginMember.getId());
        LocalDateTime checkInTime
                = DateTimeUtils.parse(requestDto.getCheckInTime());
        if(DateTimeUtils.isHoliday(checkInTime)){
            throw new AttendancesCustomException(AttendancesExceptionCode.IS_HOLIDAY);
        }

        // 당일의 체크인 타임이 적절한지
        AttendanceStatus attendanceStatus
                = validateAndDetermineCheckInStatus(checkInTime);
        attendance.updateCheckIn(
                checkInTime, attendanceStatus);

        return AttendancesConverter.toUpdateCheckInTimeDto(attendance);
    }

    @Override
    @Transactional
    public AttendancesResponseDto.UpdateCheckOutTimeDto updateCheckOutTimeForMember(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckOutTimeDto requestDto,
            Members loginMember){
        Attendances attendance = findById(attendanceId);
        if(attendance.getCheckOutTime() != null){
            throw new AttendancesCustomException(AttendancesExceptionCode.EXIST_CHECKOUT_TIME);
        }
        membersService.matchLoginMember(
                attendance.getMember(), loginMember.getId());
        LocalDateTime checkOutTime
                = DateTimeUtils.parse(requestDto.getCheckOutTime());

        if(DateTimeUtils.isHoliday(checkOutTime)){
            throw new AttendancesCustomException(AttendancesExceptionCode.IS_HOLIDAY);
        }

        // 이전 상태에 따라 다른 근태 상태로 조정
        // ex) 조퇴 -> 정시 출근 상태 변경의 방지를 위해
        AttendanceStatus beforeCheckOutStatus = attendance.getAttendanceStatus();
        AttendanceStatus afterCheckOutStatus
                = validateAndDetermineCheckOutStatus(
                        attendance.getCheckInTime(),
                checkOutTime, beforeCheckOutStatus);

        attendance.updateCheckOut(
                checkOutTime, requestDto.getDescription(),
                afterCheckOutStatus);
        return AttendancesConverter.toUpdateCheckOutTimeDto(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendancesResponseDto.ReadOneDto> readFilteredForMember(
            Long memberId, Long year, Long month, Members loginMember){
        // 1. 로그인 멤버와 조회하려는 아이디가 일치하는지?
        membersService.matchLoginMember(loginMember, memberId);

        // 2. 날짜 범위 설정
        LocalDateTime startDate = (year != null && month != null)
                ? DateTimeUtils.getStartDayOfMonth(year, month)
                : null;

        LocalDateTime endDate = (year != null && month != null)
                ? DateTimeUtils.getEndDayOfMonth(year, month)
                : null;
        // 2024-12-01T00:00:00~2024-12-31

        // 4. 동적 필터링된 데이터 조회
        List<Attendances> memberAttendanceList
                = attendancesRepository.findFilteredByMember(memberId, startDate, endDate);

        // 5. DTO 반환
        return AttendancesConverter.toReadFilteredDto(memberAttendanceList);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendancesResponseDto.ReadOneDto readOne(
            Long attendanceId, Members loginMember){
        // 1. 출결 기록의 소유자인지 확인
        Attendances attendance = findById(attendanceId);

        // 1-1. 소유자의 아이디와 로그인 사용자의 아이디와 같을 때
        if(!attendance.getMember().getId().equals(loginMember.getId())){
            // 1-2. 로그인 사용자와 hrManager 또는 사장의 아이디가 같을 때
            Members hrManagerOrCeo = membersService.findHRManager();
            if(!hrManagerOrCeo.getId().equals(loginMember.getId())){
                throw new AttendancesCustomException(AttendancesExceptionCode.RESTRICTED_ACCESS);
            }
        }

        return AttendancesConverter.toReadOneDto(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendancesResponseDto.ReadOneDto> readPageByAdmin(
            String memberName, String attendanceStatus,
            DateRange checkInRange, DateRange checkOutRange,
            Members loginMember, Pageable pageable){
        // 1. 접근할 수 있는 권한을 가지는지?
        validateAccess(loginMember);

        // 2. memberName 확인
        Members foundMember = null;
        if(memberName != null){
            foundMember = membersService.findByMemberName(memberName);
        }

        // 3. 유효한 체크인, 체크아웃 시간 설정인지 확인
        // 해당 날짜가 종료일보다 시작일이 빠른지
        DateTimeUtils.validateStartAndEndDate(
                checkInRange.getStartDate(), checkInRange.getEndDate());
        DateTimeUtils.validateStartAndEndDate(
                checkOutRange.getStartDate(), checkOutRange.getEndDate());

        // 4. AttendanceStatus 변환
        AttendanceStatus attdStatus = (attendanceStatus != null)
                ? AttendancesConverter.toAttendanceStatus(attendanceStatus)
                : null;

        // 5. 필터링된 데이터 조회
        Page<Attendances> memberAttendancePage = attendancesRepository.findFilteredByAdmin(
                foundMember.getId(),
                attdStatus,
                checkInRange != null ? checkInRange.getStartDate() : null,
                checkInRange != null ? checkInRange.getEndDate() : null,
                checkOutRange != null ? checkOutRange.getStartDate() : null,
                checkOutRange != null ? checkOutRange.getEndDate() : null,
                pageable);

        return memberAttendancePage.map(AttendancesConverter::toReadOneDto);
    }

    @Override
    @Transactional
    public AttendancesResponseDto.UpdateAttendancesStatusDto updateOneStatusByAdmin(
            Long memberId, Long attendanceId, Members loginMember,
            AttendancesRequestDto.UpdateAttendanceStatusDto requestDto) {

        // 1. 변경하려는 대상과 근태 기록이 존재하는지
        membersService.findById(memberId);
        Attendances attendance
                = attendancesRepository.findByIdAndMemberId(attendanceId, memberId)
                .orElseThrow(() -> new AttendancesCustomException(AttendancesExceptionCode.NOT_FOUND_ATTENDANCES));

        // 2. 근태 기록 변경 권한이 있는지
        Members hrManagerOrCeo = membersService.findHRManagerOrCEO(loginMember);
        if (hrManagerOrCeo == null) {
            throw new AttendancesCustomException(AttendancesExceptionCode.RESTRICTED_ACCESS);
        }

        // 3. 변경하려는 근태 상태가 적절하게 요청되었는지
        AttendanceStatus requestedAttendanceStatus
                = AttendancesConverter.toAttendanceStatus(
                        requestDto.getAttendanceStatus());

        // 4. 요청하려는 근태 상태와 변경하려는 근태 상태가 같고 요청 설명이 비어있거나 똑같다면
        if (attendance.getAttendanceStatus().equals(requestedAttendanceStatus)
                && requestDto.getDescription().equals(attendance.getDescription())) {
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.EQUALS_TO_ATTENDANCES_STATUS_AND_DESCRIPTION);
        }

        // 5. 변경 요청 처리
        LocalDate currentDate = DateTimeUtils.getCurrentDateTime().toLocalDate();
        LocalDate checkInDate = attendance.getCreatedAt().toLocalDate();
        LocalDateTime checkInTime = (checkInDate.isBefore(currentDate))
                ? checkInDate.atTime(DateTimeUtils.getCheckInTime())
                : DateTimeUtils.getTodayCheckInTime();
        LocalDateTime checkOutTime = (checkInDate.isBefore(currentDate))
                ? checkInDate.atTime(DateTimeUtils.getCheckOutTime())
                : DateTimeUtils.getTodayCheckOutTime();

        // createdAt을 조작할 필요가 없음
        handleAttendance(
                attendance.getMember(), requestedAttendanceStatus,
                requestDto.getDescription(), checkInTime, checkOutTime,
                null, attendanceId);

        // 6. DTO 반환
        return AttendancesConverter.toUpdateOneStatus(attendance);
    }

    @Override
    @Transactional
    public AttendancesResponseDto.CreateOneDto createOneByAdmin(
            AttendancesRequestDto.CreateOneDto requestDto, Members loginMember) {
        // 1. 근태 기록을 만들 수 있는 사람인지?
        validateAccess(loginMember);

        // 2. 만드려는 인원이 존재하는지?
        Members foundMember = membersService.findByMemberName(requestDto.getMemberName());

        // 3. DateRange 계산
        LocalDateTime startTime = DateTimeUtils.parse(
                requestDto.getStartDate() + DateTimeUtils.suffixZeroSeconds);
        LocalDateTime endTime = DateTimeUtils.parse(
                requestDto.getEndDate() + DateTimeUtils.suffixZeroSeconds);
        DateTimeUtils.validateStartAndEndDate(startTime, endTime);

        DateRange dateRange = new DateRange(startTime, endTime);

        // 4. 출근 상태 변환
        AttendanceStatus attendanceStatus = AttendancesConverter.toAttendanceStatus(requestDto.getAttendanceStatus());

        // 데이터 처리
        if (DateTimeUtils.isBeforeToday(startTime) && DateTimeUtils.isBeforeToday(endTime)) {
            // 오늘 이전, 오늘을 포함한 기록 처리
            handlePastAttendance(dateRange, foundMember, attendanceStatus, requestDto.getDescription());
        }else if(DateTimeUtils.isBeforeToday(startTime) && DateTimeUtils.isToday(endTime)){
            handlePastAttendance(
                    new DateRange(startTime, endTime.minusDays(1L)),
                    foundMember, attendanceStatus, requestDto.getDescription());
            handleTodayAttendance(
                    foundMember, attendanceStatus, requestDto.getDescription());
        }else if(DateTimeUtils.isToday(startTime) && DateTimeUtils.isToday(endTime)){
            handleTodayAttendance(
                    foundMember, attendanceStatus, requestDto.getDescription());
        }else if (DateTimeUtils.isToday(startTime) && DateTimeUtils.isAfterToday(endTime)) {
            // 시작 시간이 오늘이고 마감일이 오늘 이후인 기록 처리
            // 오늘의 근태 기록 하나 처리
            handleTodayAttendance(
                    foundMember, attendanceStatus, requestDto.getDescription());
            // 내일의 근태 기록 이후의 근태 기록 처리
            upcomingAttendanceRepository.saveOne(
                    foundMember.getId(), new DateRange(startTime.plusDays(1L), endTime),
                    requestDto.getDescription());
        } else if ((DateTimeUtils.isAfterToday(startTime) && DateTimeUtils.isAfterToday(endTime))) {
            // 시작일이 내일이 아닌 경우
            upcomingAttendanceRepository.saveOne(foundMember.getId(), dateRange,
                    requestDto.getDescription());
        }

        // 6. 알림 생성, 자기 자신에겐 보내지 않음.
        if(!foundMember.getId().equals(loginMember.getId())){
            notificationsService.generateEntityAndSendMessage(
                    NotificationsConverter.toNotificationData(
                            foundMember, loginMember,
                            "새로운 근태 기록을 수동으로 작성하셨습니다."),
                    NotificationType.CREATE_ATTENDANCES_MANUALLY
            );
        }

        // 7. Response DTO 반환
        return AttendancesConverter.toCreateOneForAdminDto(
                foundMember.getMemberName(), attendanceStatus, requestDto.getDescription());
    }

    @Override
    @Transactional
    public void delete(){
        LocalDateTime startOfDeletion
                = DateTimeUtils.getToday().minusYears(2).minusMonths(1);
        LocalDateTime endOfDeletion
                = DateTimeUtils.getToday().minusYears(2).minusMinutes(1);
        attendancesRepository.deleteBeforeTwoYear(startOfDeletion, endOfDeletion);
    }

    @Override
    @Transactional
    public void deleteByAdmin(
            List<Long> deleteAttendanceIdList, Members loginMember){
        // 1. 접근할 수 있는 권한이 있는지?
        validateAccess(loginMember);

        // 2. 해당하는 근태 기록 존재하는지?
        List<Long> existingIdList
                = attendancesRepository.findAllById(deleteAttendanceIdList)
                .stream().map(Attendances::getId).toList();

        // 3. 해당 아이디 리스트가 비어있지 않으면 삭제
        if(!existingIdList.isEmpty()){
            attendancesRepository.deleteAllById(existingIdList);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendancesResponseDto.ReadMonthlyDto> readFilteredByMonthlyByAdmin(
            String department, Long year, Long month,
            Pageable pageable, Members loginMember) {

        // 1. 해당 월의 시작일과 마지막 일 계산
        LocalDateTime yearMonthStartDay = DateTimeUtils.getStartDayOfMonth(year, month);
        LocalDateTime yearMonthEndDay = DateTimeUtils.getEndDayOfMonth(year, month);

        // 2. 멤버 리스트 필터링
        List<Members> filteredMembers = filteredMember(null, department);

        // 3. 멤버 ID 리스트 추출
        List<Long> memberIdList = filteredMembers.stream()
                .map(Members::getId)
                .toList();

        // 4. 근태 기록 필터링
        List<Attendances> attendanceList
                = attendancesRepository.findAllFilteredByAdmin(
                        memberIdList, yearMonthStartDay, yearMonthEndDay);

        // 5. 응답 반환
        return AttendancesConverter.toReadFilteredMonthlyDto(attendanceList, pageable.getPageNumber());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendancesResponseDto.ReadOneDto> readFilteredByDailyByAdmin(
            String department, String memberName, Long year, Long month, Long day,
            Pageable pageable, Members loginMember) {
        // 1. 해당 월의 시작일과 마지막 일 계산
        LocalDateTime customStartDay
                = DateTimeUtils.of(year, month, day);
        LocalDateTime customEndDay
                = DateTimeUtils.of(year, month, day).plusDays(1).minusSeconds(1);

        // 2. 멤버 리스트 필터링
        List<Members> filteredMembers = filteredMember(memberName, department);

        // 3. 멤버 ID 리스트 추출
        List<Long> memberIdList = filteredMembers.stream()
                .map(Members::getId)
                .toList();

        // 4. 근태 기록 필터링
        Page<Attendances> attendancePage
                = attendancesRepository.findAllFilteredByAdmin(
                memberIdList, customStartDay, customEndDay, pageable);

        // 5. 응답 반환
        return AttendancesConverter.toReadFilteredDailyDto(attendancePage);
    }

    @Transactional(readOnly = true)
    public List<AttendancesResponseDto.ReadScheduledRecordDto> readScheduledRecordByAdmin(
            String department, Members loginMember) {
        // 1. 권한 검증
        if (!membersService.isManagerOrCeo(loginMember)) {
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.RESTRICTED_ACCESS);
        }

        // 2. Redis에서 모든 데이터 가져오기
        Map<String, String> redisData
                = upcomingAttendanceRepository.getAllRawValues();
        List<AttendancesResponseDto.ReadScheduledRecordDto> recordList = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, String> entry : redisData.entrySet()) {
            String key = entry.getKey();
            String rawValue = entry.getValue();
            // 2-1. Key에서 memberId와 description 추출
            Long memberId = upcomingAttendanceRepository.extractMemberIdFromKey(key);
            String description = upcomingAttendanceRepository.extractDescriptionFromKey(key);

            if (memberId == null || description == null) continue;

            // 2-2. Value에서 DateRange 파싱
            DateRange dateRange = upcomingAttendanceRepository.deserializeValue(rawValue, DateRange.class);

            Members member;
            if (department == null) {
                member = membersService.findById(memberId);
            } else {
                member = membersService.findByIdAndDepartment(
                        memberId, membersService.findDepartment(department));
            }
            if (member == null) {
                continue;
            }
            // 2-3. DTO List 생성
            recordList.add(AttendancesConverter.toReadScheduleRecordDto(
                    index, memberId, member.getMemberName(), member.getDepartment(),
                    description, dateRange.getStartDate(), dateRange.getEndDate()));
            index++;
        }
        // 3. 결과 반환
        return recordList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendancesResponseDto.ReadSummaryOneDto> getPersonalAttendanceDtoList(
            Members loginMember) {

        DateRange attendanceDateRange
                = DateTimeUtils.setWeek(DateTimeUtils.getToday());
        List<Attendances> personalAttendanceList
                = attendancesRepository.findFilteredByMember(
                        loginMember.getId(), attendanceDateRange.getStartDate(),
                attendanceDateRange.getEndDate());

        return AttendancesConverter.toReadSummaryListDto(personalAttendanceList);
    }

    @Override
    @Transactional
    public AttendancesResponseDto.ReadTodayOneDto readTodayOne(
            Long memberId, Members loginMember) {
        membersService.matchLoginMember(loginMember, memberId);
        Attendances attendance
                = attendancesRepository.findByMemberIdAndCreatedDate(
                        memberId, DateTimeUtils.getToday().toLocalDate())
                .orElseThrow(
                        () -> new AttendancesCustomException(
                                AttendancesExceptionCode.NOT_FOUND_ATTENDANCES));

        return AttendancesConverter.toReadTodayOneDto(attendance);
    }

    @Transactional(readOnly = true)
    public Attendances findById(Long attendancesId){
        return attendancesRepository.findById(attendancesId).orElseThrow(
                ()-> new AttendancesCustomException(AttendancesExceptionCode.NOT_FOUND_ATTENDANCES)
        );
    }

    private AttendanceStatus validateAndDetermineCheckInStatus(LocalDateTime checkInTime){
        // 1. 출근 시간 유효한지?
        validateTodayCheckInOrCheckOutTime(checkInTime);

        // 2. 유효한 출근 시간인지 검증
        if(checkInTime.getHour() < 8 || checkInTime.getHour() > 14){
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.CHECK_IN_TIME_OUTSIDE_WORK_HOURS);
        }

        // 3. 오전 9시를 넘은 시간에 출근했을 때
        if(checkInTime.getHour() > DateTimeUtils.getTodayCheckInTime().getHour()
                || (checkInTime.getHour() == DateTimeUtils.getTodayCheckInTime().getHour()
                && checkInTime.getMinute() > 1)){
            return AttendanceStatus.LATE;
        }

        return AttendanceStatus.ON_TIME;
    }

    private AttendanceStatus validateAndDetermineCheckOutStatus(
            LocalDateTime checkInTime, LocalDateTime checkOutTime, AttendanceStatus beforeCheckOutStatus){
        validateTodayCheckInOrCheckOutTime(checkOutTime);

        // 1. 결석 처리: 출근-퇴근 시간이 2시간 이하
        if (!DateTimeUtils.isWithinHours(checkInTime, checkOutTime, 2)) {
            return AttendanceStatus.ABSENT;
        }

        // 2. 조퇴 처리: 퇴근 시간이 정상 퇴근 시간보다 이른 경우
        if (DateTimeUtils.isBeforeTodayCheckOutTime(checkOutTime)) {
            return AttendanceStatus.HALF_DAY;
        }

        // 3. 지각 인원은 그대로 지각 처리
        if(beforeCheckOutStatus.equals(AttendanceStatus.LATE)){
            return beforeCheckOutStatus;
        }

        // 4. 정시 출근에 일찍 퇴근을 눌렀을 경우
        if (!DateTimeUtils.isWithinHours(checkInTime, checkOutTime, 8)) {
            return AttendanceStatus.HALF_DAY;
        }

        // 5. 퇴근 시간이 유효한 시간은 오후 5시 반부터 7시까지로 고정
        if(!DateTimeUtils.isBetweenTodayCheckOutTime(checkOutTime)){
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.CHECK_OUT_TIME_OUTSIDE_WORK_HOURS);
        }

        // 5. 정상 퇴근 처리
        return AttendanceStatus.ON_TIME;
    }

    @Override
    @Transactional(readOnly = true)
    public void updateYesterdayAttendanceList(){
        List<Long> memberIdList
                = membersService.findMemberIdList();
        LocalDateTime yesterday = DateTimeUtils.getToday().minusDays(1);
        for(Long memberId : memberIdList){
            Attendances yesterdayAttendance
                    = attendancesRepository.findByMemberIdAndCreatedDate(
                            memberId, yesterday.toLocalDate()).orElse(null);
            if(yesterdayAttendance != null){
                String defaultMessage = "스케줄러에 의한 근태 기록 처리가 되었습니다.";
                switch (yesterdayAttendance.getAttendanceStatus()) {
                    case LATE, ON_TIME -> {
                        if(yesterdayAttendance.getCheckOutTime() == null){
                            String reason = defaultMessage+" / 사유 : 퇴근 미신청";
                            yesterdayAttendance.updateDescriptionAndStatus(
                                    reason, AttendanceStatus.ABSENT);
                        }
                    }
                }
            }else{
                notificationsService.generateEntityAndSendMessage(
                        NotificationsConverter.toNotificationData(
                                membersService.findItManager(), membersService.findCeo(),
                                DateTimeUtils.getToday()+" 근태 기록 스케줄링 미작동"),
                        NotificationType.URGENT_SERVER_ERROR
                );
                return;
            }
        }
    }

    private void validateTodayCheckInOrCheckOutTime(LocalDateTime time){
        if(!DateTimeUtils.isToday(time)){
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.TIME_BEFORE_TODAY);
        }
    }

    @Override
    @Transactional
    public void sendNotificationForUpcomingAttendance() {
        List<Long> memberIdList = membersService.findMemberIdList();
        Map<Long, String> memberIdUpcomingAttendanceMap
                = upcomingAttendanceRepository.getStartDatesByMemberIds(memberIdList);

        Members hrManager = membersService.findHRManager();

        memberIdUpcomingAttendanceMap.forEach((memberId, upcomingAttendance) -> {
            String formattedDate = DateTimeUtils.getFormattedDate(upcomingAttendance);
            LocalDateTime outOfOfficeTime = DateTimeUtils.parse(formattedDate);
            if(DateTimeUtils.isTomorrow(outOfOfficeTime)){
                Members toMember = membersService.findById(memberId);
                String message = "내일 외근 일정이 잡혀 있습니다. 꼭 확인해주세요!";
                notificationsService.generateEntityAndSendMessage(
                        NotificationsConverter.toNotificationData(
                                toMember, hrManager, message),
                        NotificationType.UPCOMING_OUT_OFF_OFFICE_NOTIFICATION);
            }
        });
    }
    
    private void validateAccess(Members loginMember){
        Members hrManagerOrCeo = membersService.findHRManagerOrCEO(loginMember);
        if(hrManagerOrCeo == null){
            throw new AttendancesCustomException(AttendancesExceptionCode.RESTRICTED_ACCESS);
        }
    }

    private List<Members> filteredMember(String memberName, String department){
        if (memberName == null && department == null) {
             return membersService.findAll();
        } else if (memberName != null && department == null) {
            return membersService.findAllByMemberName(memberName);
        } else if (memberName == null && department != null) {
            return membersService.findAllByDepartment(
                    membersService.findDepartment(department));
        } else {
            return membersService.findAllByDepartment(
                    membersService.findDepartment(department), memberName);
        }
    }

    private void handleAttendance(
            Members member, AttendanceStatus status, String description,
            LocalDateTime checkInTime, LocalDateTime checkOutTime,
            LocalDateTime customCreatedAt, Long attendanceId) {

        // 해당 날짜에 휴가가 존재하는데, 다른 status로 변경하려는 경우
        List<Vacations> vacationList
                = vacationsService.findAcceptedVacationByMemberIdAndDateRange(
                        member.getId(), true, checkInTime, checkOutTime);
        if(!vacationList.isEmpty() && !status.equals(AttendanceStatus.VACATION)){
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.EXIST_VACATION);
        }

        // 해당 날자에 휴가가 존재하지 않는데, 휴가로 상태를 변경하려는 경우
        if(vacationList.isEmpty() && status.equals(AttendanceStatus.VACATION)){
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.NOT_CHANGE_ATTENDANCES_STATUS_VACATION);
        }

        LocalDateTime lateCheckInTime
                = LocalDateTime.of(checkInTime.toLocalDate(),
                DateTimeUtils.getCheckInTime()).plusSeconds(1);
        LocalDateTime halfDayCheckOutTime
                = LocalDateTime.of(checkInTime.toLocalDate(),
                DateTimeUtils.getCheckInTime().plusHours(4));
        // 1. 수동으로 CreatedAt를 조작하여 만들 것인지? 아닌지
        if(customCreatedAt != null){
            // 1-1. 수동으로 조작하여 만듦, attendanceId를 애초에 null로 가져와서 해당 근태가 없음.
            Attendances attendance = attendancesRepository.findByMemberIdAndCreatedDate(
                    member.getId(), customCreatedAt.toLocalDate()).orElse(null);
            if(attendance != null){
                throw new AttendancesCustomException(AttendancesExceptionCode.DUPLICATED_ATTENDANCE);
            }
            switch (status) {
                case ABSENT, VACATION, HOLIDAY ->
                    // 결근, 외근, 휴가 상태: checkInTime과 checkOutTime을 null로 설정
                    attendancesRepository.saveManually(
                            member.getId(), customCreatedAt,
                            AttendancesConverter.toEntity(
                                    member, status, description,
                                    null, null));
                case LATE -> {
                    if (DateTimeUtils.isBeforeTodayCheckOutTime(
                            DateTimeUtils.getCurrentDateTime())) {
                        attendancesRepository.saveManually(member.getId(), customCreatedAt,
                                AttendancesConverter.toEntity(
                                        member, status, description,
                                        lateCheckInTime, null));
                    } else {
                        attendancesRepository.saveManually(member.getId(), customCreatedAt,
                                AttendancesConverter.toEntity(
                                        member, status, description, lateCheckInTime, checkOutTime));
                    }
                }
                case HALF_DAY ->
                    // 조퇴면 09시~ 오후 1시퇴근 고정
                    attendancesRepository.saveManually(
                            member.getId(), customCreatedAt,
                            AttendancesConverter.toEntity(
                                    member, status, description, checkInTime,
                                    halfDayCheckOutTime));
                case ON_TIME -> {
                    if(DateTimeUtils.isToday(customCreatedAt)
                            && DateTimeUtils.isBeforeTodayCheckOutTime(DateTimeUtils.getCurrentDateTime())) {
                        attendancesRepository.saveManually(
                                member.getId(), customCreatedAt,
                                AttendancesConverter.toEntity(
                                        member, status, description, checkInTime,
                                        null));
                    }else{
                        attendancesRepository.saveManually(
                                member.getId(), customCreatedAt,
                                AttendancesConverter.toEntity(
                                        member, status, description,
                                        checkInTime, checkOutTime));
                    }
                }
                case OUT_OF_OFFICE ->
                    attendancesRepository.saveManually(
                            member.getId(), customCreatedAt,
                            AttendancesConverter.toEntity(
                                    member, status, description,
                                    checkInTime, checkOutTime));
                default ->
                    throw new AttendancesCustomException(
                            AttendancesExceptionCode.NOT_FOUND_ATTENDANCE_STATUS);
            }
        }else{
            // 1-2. 조작하지 않고 근태 기록의 수정만 필요
            Attendances attendance = findById(attendanceId);

            switch (status) {
                case ABSENT, VACATION, HOLIDAY ->
                    // 결근, 외근, 휴가 상태: checkInTime과 checkOutTime을 null로 설정
                    attendance.update(
                            status, description, null, null);
                case LATE -> {
                    LocalDateTime currentTime = DateTimeUtils.getCurrentDateTime();
                    if(DateTimeUtils.isToday(lateCheckInTime)
                            && currentTime.isBefore(DateTimeUtils.getTodayCheckOutTime())){
                        attendance.update(
                                status, description, lateCheckInTime, null);
                    }else{
                        // 어제 이전의 기간, 오늘인데 오늘 퇴근 시간을 지난 경우
                        attendance.update(
                                status, description, lateCheckInTime, checkOutTime);
                    }
                }
                case HALF_DAY -> {
                    LocalDateTime currentTime = DateTimeUtils.getCurrentDateTime();
                    LocalDateTime todayHalfDayTime
                            = DateTimeUtils.getStartTimeOfHalfDay(
                                    currentTime.getYear(), currentTime.getMonthValue(),
                            currentTime.getDayOfMonth());

                    // 오늘인 기록을 수정
                    if(DateTimeUtils.isToday(checkInTime) && !currentTime.isBefore(todayHalfDayTime)){
                        attendance.update(
                                status, description, checkInTime, currentTime);
                    }else{
                        // 이전 기록을 수정
                        if(!DateTimeUtils.isToday(checkInTime)){
                            attendance.update(
                                    status, description, checkInTime, LocalDateTime.of(
                                            checkInTime.getYear(), checkInTime.getMonthValue(),
                                            checkInTime.getDayOfMonth(), 13, 0, 0));
                        }else{
                            throw new AttendancesCustomException(
                                    AttendancesExceptionCode.TIME_BEFORE_HALF_DAY);
                        }
                    }
                }
                case OUT_OF_OFFICE ->
                    attendance.update(
                            status, description, checkInTime, checkOutTime);
                case ON_TIME -> {
                    LocalDateTime currentTime = DateTimeUtils.getCurrentDateTime();

                    if(DateTimeUtils.isToday(checkInTime)
                            && currentTime.isBefore(DateTimeUtils.getTodayCheckOutTime())){
                        attendance.update(
                                status, description, checkInTime, null);
                    }else{
                        attendance.update(
                                status, description, checkInTime, checkOutTime);
                    }
                }
                default ->
                    throw new AttendancesCustomException(
                            AttendancesExceptionCode.NOT_FOUND_ATTENDANCE_STATUS);
            }
        }
    }

    private void handlePastAttendance(DateRange dateRange, Members member, AttendanceStatus status, String description) {
        List<DateRange> pastDateRanges
                = dateRange.splitUntil(dateRange.getEndDate()); // 오늘 이전의 데이터 추출

        for (DateRange range : pastDateRanges) {
            LocalDateTime pastCheckInTime
                    = range.getStartDate().toLocalDate()
                    .atTime(9, 0, 0);
            LocalDateTime pastCheckOutTime
                    = range.getStartDate().toLocalDate()
                    .atTime(18, 0, 0);
            LocalDateTime customCreatedAt
                    = range.getStartDate().toLocalDate()
                    .atTime(0, 0, 0);

            switch (status) {
                case ON_TIME, OUT_OF_OFFICE, ABSENT, HOLIDAY, VACATION ->
                        handleAttendance(
                                member, status, description,
                                pastCheckInTime, pastCheckOutTime,
                                customCreatedAt, null);
                case LATE ->
                        handleAttendance(
                                member, status, description,
                                pastCheckInTime.plusMinutes(1), pastCheckOutTime,
                                customCreatedAt, null);
                case HALF_DAY ->
                        handleAttendance(
                                member, status, description,
                                pastCheckInTime, pastCheckInTime.plusHours(4),
                                customCreatedAt, null);
                default ->
                        throw new AttendancesCustomException(
                                AttendancesExceptionCode.NOT_FOUND_ATTENDANCE_STATUS);
            }
        }
    }

    private void handleTodayAttendance(Members member, AttendanceStatus status, String description) {
        LocalDateTime todayCheckInTime = DateTimeUtils.getTodayCheckInTime();
        LocalDateTime todayCheckOutTime = DateTimeUtils.getTodayCheckOutTime();

        switch (status) {
            case ON_TIME, OUT_OF_OFFICE, ABSENT, HOLIDAY, VACATION ->
                    handleAttendance(
                            member, status, description,
                            todayCheckInTime, todayCheckOutTime,
                            DateTimeUtils.getToday(), null);
            case LATE ->
                    handleAttendance(
                            member, status, description,
                            todayCheckInTime.plusMinutes(1), todayCheckOutTime,
                            DateTimeUtils.getToday(), null);
            case HALF_DAY ->
                    handleAttendance(
                            member, status, description,
                            todayCheckInTime, todayCheckInTime.plusHours(4),
                            DateTimeUtils.getToday(), null);
            default ->
                    throw new AttendancesCustomException(
                            AttendancesExceptionCode.NOT_FOUND_ATTENDANCE_STATUS);
        }
    }
}
