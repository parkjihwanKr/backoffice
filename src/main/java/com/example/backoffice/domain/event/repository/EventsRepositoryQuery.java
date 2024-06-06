package com.example.backoffice.domain.event.repository;

import java.time.LocalDateTime;

public interface EventsRepositoryQuery {

    Long countVacationingMembers(LocalDateTime customStartDate);

}
