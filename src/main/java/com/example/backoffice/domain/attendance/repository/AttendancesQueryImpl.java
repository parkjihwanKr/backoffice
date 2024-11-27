package com.example.backoffice.domain.attendance.repository;

import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.attendance.entity.QAttendances;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AttendancesQueryImpl extends QuerydslRepositorySupport implements AttendancesQuery {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAttendances qAttendance = QAttendances.attendances;

    public AttendancesQueryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Attendances.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendances> findFiltered(
            Long memberId, LocalDateTime startDate,
            LocalDateTime endDate, AttendanceStatus attdStatus) {
        BooleanBuilder builder = buildFilters(memberId, attdStatus, startDate, endDate, null, null);
        return executeQuery(builder, null).fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attendances> findFilteredForAdmin(
            Long foundMemberId, AttendanceStatus attendanceStatus,
            LocalDateTime checkInStartTime, LocalDateTime checkInEndTime,
            LocalDateTime checkOutStartTime, LocalDateTime checkOutEndTime,
            Pageable pageable) {

        BooleanBuilder builder = buildFilters(foundMemberId, attendanceStatus, checkInStartTime, checkInEndTime, checkOutStartTime, checkOutEndTime);
        JPAQuery<Attendances> query = executeQuery(builder, pageable);

        long total = query.fetchCount();
        List<Attendances> content = query.fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional
    public void deleteBeforeTwoYear(
            List<Long> allMemberIdList,
            LocalDateTime startOfDeletion, LocalDateTime endOfDeletion){
        jpaQueryFactory
                .delete(qAttendance)
                .where(
                        qAttendance.member.id.in(allMemberIdList)
                                .and(qAttendance.createdAt.between(startOfDeletion, endOfDeletion))
                )
                .execute();
    }

    @Override
    @Transactional
    public Attendances findByMemberIdAndCreatedDate(
            Long memberId, LocalDate createdDate) {
        return jpaQueryFactory
                .selectFrom(qAttendance)
                .where(
                        qAttendance.member.id.eq(memberId),
                        qAttendance.createdAt.year().eq(createdDate.getYear()),
                        qAttendance.createdAt.month().eq(createdDate.getMonthValue()),
                        qAttendance.createdAt.dayOfMonth().eq(createdDate.getDayOfMonth())
                )
                .fetchOne();
    }

    @Override
    @Transactional
    public void saveManually(
            Long memberId, LocalDateTime customCreatedAt,
            Attendances attendance) {
        jpaQueryFactory.insert(qAttendance)
                .columns(qAttendance.member, qAttendance.attendanceStatus,
                        qAttendance.description, qAttendance.checkInTime,
                        qAttendance.checkOutTime, qAttendance.createdAt)
                .values(
                        attendance.getMember(),
                        attendance.getAttendanceStatus(),
                        attendance.getDescription(),
                        attendance.getCheckInTime(),
                        attendance.getCheckOutTime(),
                        customCreatedAt
                )
                .execute();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attendances> findAllFilteredByMonth(
            List<Long> memberIdList, LocalDateTime yearMonthStartDay,
            LocalDateTime yearMonthEndDay, Pageable pageable) {

        // 1. BooleanBuilder를 사용해 필터 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        if (memberIdList != null && !memberIdList.isEmpty()) {
            builder.and(qAttendance.member.id.in(memberIdList));
        }
        builder.and(qAttendance.createdAt.between(yearMonthStartDay, yearMonthEndDay));

        // 2. Query 생성
        JPAQuery<Attendances> query = jpaQueryFactory
                .selectFrom(qAttendance)
                .where(builder);

        // 3. 페이징 처리
        long total = query.fetchCount(); // 전체 결과 개수
        List<Attendances> content = getQuerydsl()
                .applyPagination(pageable, query)
                .fetch();

        // 4. PageImpl로 결과 반환
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanBuilder buildFilters(
            Long memberId, AttendanceStatus attendanceStatus,
            LocalDateTime checkInStartTime, LocalDateTime checkInEndTime,
            LocalDateTime checkOutStartTime, LocalDateTime checkOutEndTime) {

        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null) {
            builder.and(qAttendance.member.id.eq(memberId));
        }
        if (attendanceStatus != null) {
            builder.and(qAttendance.attendanceStatus.eq(attendanceStatus));
        }
        if (checkInStartTime != null) {
            builder.and(qAttendance.checkInTime.goe(checkInStartTime));
        }
        if (checkInEndTime != null) {
            builder.and(qAttendance.checkInTime.loe(checkInEndTime));
        }
        if (checkOutStartTime != null) {
            builder.and(qAttendance.checkOutTime.goe(checkOutStartTime));
        }
        if (checkOutEndTime != null) {
            builder.and(qAttendance.checkOutTime.loe(checkOutEndTime));
        }

        return builder;
    }

    private JPAQuery<Attendances> executeQuery(BooleanBuilder builder, Pageable pageable) {
        JPAQuery<Attendances> query = jpaQueryFactory
                .selectFrom(qAttendance)
                .where(builder);

        if (pageable != null) {
            return (JPAQuery<Attendances>) getQuerydsl().applyPagination(pageable, query);
        }

        return query;
    }
}
