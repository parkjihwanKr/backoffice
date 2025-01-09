package com.example.backoffice.domain.attendance.repository;

import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.attendance.entity.QAttendances;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class AttendancesQueryImpl extends QuerydslRepositorySupport implements AttendancesQuery {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAttendances qAttendance = QAttendances.attendances;

    @PersistenceContext
    private EntityManager entityManager;

    public AttendancesQueryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Attendances.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendances> findFiltered(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate) {

        // startDate : year:month:01T00:00:00
        // endDate : year:month:monthOfLastDayT:23:59:59
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(
                qAttendance.createdAt.goe(startDate)
                .and(qAttendance.createdAt.loe(endDate))
                .and(qAttendance.member.id.eq(memberId)));

        return jpaQueryFactory.selectFrom(qAttendance)
                .where(builder)
                .fetch();
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
    public Optional<Attendances> findByMemberIdAndCreatedDate(
            Long memberId, LocalDate createdDate) {
        Attendances attendance = jpaQueryFactory
                .selectFrom(qAttendance)
                .where(
                        qAttendance.member.id.eq(memberId),
                        qAttendance.createdAt.year().eq(createdDate.getYear()),
                        qAttendance.createdAt.month().eq(createdDate.getMonthValue()),
                        qAttendance.createdAt.dayOfMonth().eq(createdDate.getDayOfMonth())
                )
                .fetchOne();

        return Optional.ofNullable(attendance); // Optional로 감싸서 반환
    }

    @Override
    @Transactional
    public void saveManually(Long memberId, LocalDateTime customCreatedAt, Attendances attendance) {
        String sql
                = "INSERT INTO attendances (member_id, attendance_status, description, check_in_time, check_out_time, created_at, modified_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Query query = entityManager.createNativeQuery(sql)
                .setParameter(1, memberId)
                .setParameter(2, attendance.getAttendanceStatus().name())
                .setParameter(3, attendance.getDescription())
                .setParameter(4, attendance.getCheckInTime())
                .setParameter(5, attendance.getCheckOutTime())
                .setParameter(6, customCreatedAt)
                .setParameter(7, customCreatedAt);

        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendances> findAllFiltered(
            List<Long> memberIdList, LocalDateTime customStartDay,
            LocalDateTime customEndDay) {

        // BooleanBuilder를 사용해 필터 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        if (memberIdList != null && !memberIdList.isEmpty()) {
            builder.and(qAttendance.member.id.in(memberIdList));
        }

        builder.and(qAttendance.createdAt.between(customStartDay, customEndDay));

        // Query 실행
        return jpaQueryFactory
                .selectFrom(qAttendance)
                .where(builder)
                .fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attendances> findAllFiltered(
            List<Long> memberIdList, LocalDateTime customStartDay,
            LocalDateTime customEndDay, Pageable pageable) {

        // 1. BooleanBuilder를 사용해 필터 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        if (memberIdList != null && !memberIdList.isEmpty()) {
            builder.and(qAttendance.member.id.in(memberIdList));
        }

        builder.and(qAttendance.createdAt.between(customStartDay, customEndDay));

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
