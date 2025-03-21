package com.example.backoffice.domain.attendances;

import com.example.backoffice.domain.attendance.converter.AttendancesConverter;
import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.member.entity.Members;
import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class AttendancesServiceTest {

    /*    @PostConstruct
    @Transactional
    public void test2(){
        attendancesRepository.bulkDeleteByIdGreaterThanEqual(131L);
    }*/

    /*@Transactional
    @PostConstruct
    public void test() {
        List<Members> memberList = membersService.findAll();
        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2025, 2, 28);
        long totalRecords = 87500;
        Random random = new Random();
        Set<LocalDate> holidays = getHolidays(startDate, endDate);
        int memberIndex = 0;

        for (long i = 0; i < totalRecords; i++) {
            Members member = memberList.get(memberIndex);
            memberIndex = (memberIndex + 1) % memberList.size(); // 멤버를 순차적으로 순환

            LocalDate date = startDate.plusDays(i % ChronoUnit.DAYS.between(startDate, endDate));
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            boolean isHoliday = holidays.contains(date) || dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;

            AttendanceStatus status;
            LocalDateTime checkInTime = null;
            LocalDateTime checkOutTime = null;

            if (isHoliday) {
                status = AttendanceStatus.HOLIDAY;
            } else {
                status = getRandomStatus(random);
                switch (status) {
                    case ABSENT:
                    case VACATION:
                    case HOLIDAY:
                        checkInTime = null;
                        checkOutTime = null;
                        break;
                    case HALF_DAY:
                        checkInTime = date.atTime(9, 0);
                        checkOutTime = date.atTime(13, 0);
                        break;
                    case OUT_OF_OFFICE:
                    case ON_TIME:
                        checkInTime = date.atTime(9, 0);
                        checkOutTime = date.atTime(18, 0);
                        break;
                    case LATE:
                        checkInTime = date.atTime(10, 0);
                        checkOutTime = date.atTime(18, 0);
                        break;
                }
            }

            Attendances attendances = AttendancesConverter.toEntity(member, status, "description" + i, checkInTime, checkOutTime);

            attendancesRepository.saveManually(
                    member.getId(),
                    checkInTime != null ?
                            checkInTime.toLocalDate().atTime(0, 0, 0)
                            : date.atTime(0,0,0),
                    attendances
            );
        }
    }

    *//**
     * 2023-02-01 ~ 2025-02-28 사이의 공휴일 목록을 반환
     *//*
    private Set<LocalDate> getHolidays(LocalDate start, LocalDate end) {
        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(2023, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2023, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2023, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2023, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2023, 10, 9)); // 한글날
        holidays.add(LocalDate.of(2023, 12, 25)); // 크리스마스

        // 2024년, 2025년도 공휴일 추가
        holidays.add(LocalDate.of(2024, 1, 1));  // 신정
        holidays.add(LocalDate.of(2024, 2, 9));  // 설날 연휴 시작
        holidays.add(LocalDate.of(2024, 2, 10)); // 설날
        holidays.add(LocalDate.of(2024, 2, 11)); // 설날 연휴
        holidays.add(LocalDate.of(2024, 4, 10)); // 국회의원 선거일
        holidays.add(LocalDate.of(2024, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2024, 5, 15)); // 부처님오신날
        holidays.add(LocalDate.of(2024, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2024, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2024, 9, 16)); // 추석 연휴 시작
        holidays.add(LocalDate.of(2024, 9, 17)); // 추석
        holidays.add(LocalDate.of(2024, 9, 18)); // 추석 연휴
        holidays.add(LocalDate.of(2024, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2024, 10, 9)); // 한글날
        holidays.add(LocalDate.of(2024, 12, 25)); // 크리스마스

        holidays.add(LocalDate.of(2025, 1, 1));  // 신정
        holidays.add(LocalDate.of(2025, 1, 28)); // 설날
        holidays.add(LocalDate.of(2025, 1, 29)); // 설날 연휴
        holidays.add(LocalDate.of(2025, 1, 30)); // 설날 연휴 끝
        holidays.add(LocalDate.of(2025, 3, 1));  // 삼일절
        holidays.add(LocalDate.of(2025, 5, 5));  // 어린이날
        holidays.add(LocalDate.of(2025, 6, 6));  // 현충일
        holidays.add(LocalDate.of(2025, 8, 15)); // 광복절
        holidays.add(LocalDate.of(2025, 10, 3)); // 개천절
        holidays.add(LocalDate.of(2025, 10, 9)); // 한글날
        holidays.add(LocalDate.of(2025, 12, 25)); // 크리스마스

        return holidays;
    }

    *//**
     * 랜덤한 AttendanceStatus를 반환 (HOLIDAY 제외)
     *//*
    private AttendanceStatus getRandomStatus(Random random) {
        AttendanceStatus[] statuses = Arrays.stream(AttendanceStatus.values())
                .filter(s -> s != AttendanceStatus.HOLIDAY)
                .toArray(AttendanceStatus[]::new);
        return statuses[random.nextInt(statuses.length)];
    }*/
}
