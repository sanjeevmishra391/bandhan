package com.bandhan.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandhan.entity.BookingDate;
import com.bandhan.entity.BookingDateId;

import jakarta.transaction.Transactional;

public interface BookingDateRepository extends JpaRepository<BookingDate, BookingDateId> {
    @Query(nativeQuery = true, value = """
        SELECT * FROM booking_dates 
        WHERE calendar_date = :calendarDate 
        AND (
            (:startTime BETWEEN start_time AND end_time)  -- Case 1: New start time falls inside an existing booking
            OR (:endTime BETWEEN start_time AND end_time) -- Case 2: New end time falls inside an existing booking
            OR (start_time BETWEEN :startTime AND :endTime) -- Case 3: Existing booking starts inside the new slot
            OR (end_time BETWEEN :startTime AND :endTime) -- Case 4: Existing booking ends inside the new slot
        )
    """)
    List<BookingDate> findOverlappingBookings(@Param("calendarDate") LocalDate calendarDate, @Param("startTime") LocalTime starTime, @Param("endTime") LocalTime endTime);

    List<BookingDate> findById_CalendarDate(LocalDate calendarDate);

    @Modifying
    @Transactional
    @Query("DELETE FROM BookingDate bd WHERE bd.booking.id = :bookingId")
    int deleteAllByBookingId(Long bookingId);

    @Query(nativeQuery = true, value = """
        SELECT * FROM booking_dates
        WHERE calendar_date = :calendarDate
        AND booking_id <> :bookingId
    """)
    List<BookingDate> findByCalendarDateAndNotBookingId(@Param("calendarDate") LocalDate calendarDate, @Param("bookingId") Long bookingId);

}
