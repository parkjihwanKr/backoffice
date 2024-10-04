package com.example.backoffice.domain.vacation.repository;

import com.example.backoffice.domain.vacation.entity.QVacations;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class VacationsQueryImpl extends QuerydslRepositorySupport implements VacationsQuery{

    private final JPAQueryFactory jpaQueryFactory;

    QVacations qVacations = QVacations.vacations;
    public VacationsQueryImpl(JPAQueryFactory jpaQueryFactory){
        super(Vacations.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public long countVacationingMembers(LocalDateTime customStartDate) {
        Long count = jpaQueryFactory
                .select(qVacations.count())
                .from(qVacations)
                .where(qVacations.startDate.loe(customStartDate)
                        .and(qVacations.endDate.goe(customStartDate)))
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public List<Vacations> findVacationsOnDate(LocalDateTime startDate){
        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(qVacations.startDate.loe(startDate)
                        .and(qVacations.endDate.goe(startDate)))
                .fetch();
    }

    @Override
    public List<Vacations> findVacationsOnMonth(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(qVacations.startDate.loe(endDate)
                        .and(qVacations.endDate.goe(startDate)))
                .fetch();
    }

    @Override
    public List<Vacations> findAllByEndDateBefore(LocalDateTime now){
        LocalDateTime endOfYesterday
                = now.minusDays(1).withHour(23).withMinute(59).withSecond(59);

        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(qVacations.endDate.before(endOfYesterday))
                .fetch();
    }

    @Override
    public List<Vacations> findAllByStartDate(LocalDateTime now) {
        // 하루 날짜를 여기서 설정
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);

        return jpaQueryFactory
                .selectFrom(qVacations)
                .where(qVacations.startDate.between(startOfDay, endOfDay))
                .fetch();
    }
}