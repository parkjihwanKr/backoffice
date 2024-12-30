package com.example.backoffice.domain.event.repository;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.entity.QEvents;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventsRepositoryQueryImpl extends QuerydslRepositorySupport implements EventsRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    QEvents qEvents = QEvents.events;

    public EventsRepositoryQueryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Events.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // EventType 필터링
    private BooleanExpression eventTypeEq(EventType eventType) {
        return eventType != null ? qEvents.eventType.eq(eventType) : null;
    }

    // 부서 필터링
    private BooleanExpression departmentEq(MemberDepartment department) {
        return department != null ? qEvents.department.eq(department) : null;
    }

    @Override
    public List<Events> findFiltered(
            EventType eventType, MemberDepartment department,
            LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .selectFrom(qEvents)
                .where(
                        eventTypeEq(eventType), // EventType 필터링
                        departmentEq(department), // 부서 필터링
                        qEvents.startDate.loe(end),  // 시작일이 endDate보다 작거나 같음
                        qEvents.endDate.goe(start)   // 종료일이 startDate보다 크거나 같음확인
                ).fetch();
    }
}
