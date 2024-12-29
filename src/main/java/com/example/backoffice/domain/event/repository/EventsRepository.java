package com.example.backoffice.domain.event.repository;

import com.example.backoffice.domain.event.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Events, Long>, EventsRepositoryQuery {

}
