package com.bandhan.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bandhan.entity.CalendarDate;
import com.bandhan.enums.CalendarDateStatus;

public interface CalendarDateRepository extends JpaRepository<CalendarDate, LocalDate> {
    List<CalendarDate> findByStatus(CalendarDateStatus status);
}