package com.example.backoffice.domain.vacation.repository;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.vacation.entity.QVacations;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class VacationsQueryImpl extends QuerydslRepositorySupport implements VacationsQuery {

    private final JPAQueryFactory jpaQueryFactory;
    private final QVacations qVacations = QVacations.vacations;

    public VacationsQueryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Vacations.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // 휴가 기간 겹침 여부를 판단하는 BooleanExpression
    private BooleanExpression vacationDateOverlap(LocalDateTime startDate, LocalDateTime endDate) {
        return qVacations.startDate.loe(endDate)
                .and(qVacations.endDate.goe(startDate));
    }

    private BooleanExpression isAccepted(Boolean isAccepted) {
        return isAccepted != null ? qVacations.isAccepted.eq(isAccepted) : null;
    }

    private BooleanExpression isUrgent(Boolean urgent) {
        return urgent != null ? qVacations.urgent.eq(urgent) : null;
    }

    private BooleanExpression filterByDepartment(MemberDepartment department) {
        return department != null ? qVacations.onVacationMember.department.eq(department) : null;
    }

    @Override
    public long countVacationingMembers(LocalDateTime customStartDate) {
        Long count = jpaQueryFactory
                .select(qVacations.count())
                .from(qVacations)
                .where(vacationDateOverlap(customStartDate, customStartDate)) // customStartDate에 휴가 중인 멤버 수 계산
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public List<Vacations> findVacationsOnDate(LocalDateTime startDate) {
        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(vacationDateOverlap(startDate, startDate)) // 특정 날짜에 휴가 중인 멤버를 찾음
                .fetch();
    }

    @Override
    public List<Vacations> findVacationsOnMonth(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(vacationDateOverlap(startDate, endDate)) // 한 달 동안 휴가 중인 멤버 찾기
                .fetch();
    }

    @Override
    public List<Vacations> findAllByEndDateBefore(LocalDateTime now) {
        LocalDateTime endOfYesterday = now.minusDays(1).withHour(23).withMinute(59).withSecond(59);

        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(qVacations.endDate.before(endOfYesterday)) // 어제까지 끝난 모든 휴가 찾기
                .fetch();
    }

    @Override
    public List<Vacations> findAllByStartDate(LocalDateTime now) {
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);

        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(qVacations.startDate.between(startOfDay, endOfDay)) // 오늘 시작하는 모든 휴가 찾기
                .fetch();
    }

    @Override
    public List<Vacations> findAcceptedVacationByMemberIdAndDateRange(
            Long memberId, Boolean isAccepted, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(qVacations.onVacationMember.id.eq(memberId)
                        .and(qVacations.isAccepted.eq(isAccepted))
                        .and(vacationDateOverlap(startDate, endDate))) // 휴가 승인 여부와 날짜 범위로 검색
                .fetch();
    }

    @Override
    public List<Vacations> findAllByMemberIdAndStartDate(Long memberId, LocalDateTime startDate) {
        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(qVacations.onVacationMember.id.eq(memberId)
                        .and(qVacations.startDate.goe(startDate))) // 멤버 ID와 휴가 시작일로 휴가 검색
                .fetch();
    }

    @Override
    public Boolean existsVacationForMemberInDateRange(
            Long vacationId, Long memberId, LocalDateTime startDate, LocalDateTime endDate){
        if(vacationId == null){
            // VACATION_CRUD_TYPE : CREATE
            return jpaQueryFactory
                    .selectOne()
                    .from(qVacations)
                    .where(qVacations.onVacationMember.id.eq(memberId)
                            .and(vacationDateOverlap(startDate, endDate))
                    ).fetchFirst() != null;
        }else{
            // VACATION_CRUD_TYPE : UPDATE
            // 해당 사항은 변경할 휴가를 제외하고 겹치는 휴가 기간이 있는지를 확인해야함
            return jpaQueryFactory
                    .selectOne()
                    .from(qVacations)
                    .where(qVacations.id.ne(vacationId)
                            .and(qVacations.onVacationMember.id.eq(memberId))
                            .and(vacationDateOverlap(startDate, endDate))
                    ).fetchFirst() != null;
        }
    }

    @Override
    public List<Vacations> findAllByIsAccepted(
            Boolean isAccepted, LocalDateTime startDate){
        return jpaQueryFactory.selectFrom(qVacations)
                .where(qVacations.isAccepted.eq(isAccepted)
                        .and(qVacations.startDate.goe(startDate)))
                .fetch();
    }

    @Override
    public List<Vacations> findFilteredVacationsOnMonth(
            LocalDateTime startDate, LocalDateTime endDate,
            Boolean isAccepted, Boolean urgent, MemberDepartment memberDepartment){
        if(memberDepartment == null){
            return jpaQueryFactory.selectFrom(qVacations)
                    .where(vacationDateOverlap(startDate, endDate),
                            isAccepted(isAccepted),
                            isUrgent(urgent))
                    .fetch();
        }
        return jpaQueryFactory.selectFrom(qVacations)
                .where(vacationDateOverlap(startDate, endDate),
                        isAccepted(isAccepted),
                        isUrgent(urgent),
                        filterByDepartment(memberDepartment))
                .fetch();
    }
}
