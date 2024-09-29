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

    @Override
    public Long countVacationingMembers(LocalDateTime currentDate) {
        // currentDate보다 작거나 같을 때 && currentDate보다 크거나 같을 때 && EventType.MEMBER_VACATION 일 때
        return jpaQueryFactory.selectFrom(qEvents)
                .where(qEvents.eventType.eq(EventType.MEMBER_VACATION)
                        // loe : less than or equals to
                        .and(qEvents.startDate.loe(currentDate))
                        // goe : greater than or equals to
                        .and(qEvents.endDate.goe(currentDate)))
                .fetchCount();
    }

    // EventType 필터링
    private BooleanExpression eventTypeEq(EventType eventType) {
        return eventType != null ? qEvents.eventType.eq(eventType) : null;
    }

    // 부서 필터링
    private BooleanExpression departmentEq(MemberDepartment department) {
        return department != null ? qEvents.department.eq(department) : null;
    }

    // 시작 또는 끝 날짜가 범위에 있는지 확인
    private BooleanExpression startOrEndDateBetween(LocalDateTime start, LocalDateTime end) {
        return qEvents.startDate.between(start, end)
                .or(qEvents.endDate.between(start, end));
    }

    @Override
    public List<Events> findAllByEventTypeAndDepartmentAndStartDateOrEndDateBetween(
            EventType eventType, MemberDepartment department, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .selectFrom(qEvents)
                .where(
                        eventTypeEq(eventType), // EventType 필터링
                        departmentEq(department), // 부서 필터링
                        startOrEndDateBetween(start, end) // 시작 또는 끝 날짜가 해당 범위에 있는지 확인
                )
                .fetch();
    }

    @Override
    public List<Events> findAllByMemberIdAndEventTypeAndDateRange(
            Long memberId, EventType eventType,
            LocalDateTime start, LocalDateTime end){
        return jpaQueryFactory
                .selectFrom(qEvents)
                .where(qEvents.member.id.eq(memberId)
                        .and(qEvents.eventType.eq(eventType))
                        .and(qEvents.startDate.between(start, end)))
                .fetch();
    }
}
