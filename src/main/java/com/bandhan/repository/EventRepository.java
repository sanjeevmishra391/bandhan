package com.bandhan.repository;

import com.bandhan.entity.Event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bandhan.enums.EventStatus;



public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNameContainingIgnoreCase(String name);
    List<Event> findByEventStatus(EventStatus eventStatus);
}
