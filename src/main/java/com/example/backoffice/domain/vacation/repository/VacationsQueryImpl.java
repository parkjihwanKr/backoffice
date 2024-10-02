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
}