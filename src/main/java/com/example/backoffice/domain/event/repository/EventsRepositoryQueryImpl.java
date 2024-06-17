package com.example.backoffice.domain.event.repository;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.event.entity.QEvents;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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
}
