package com.bandhan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bandhan.entity.Booking;
import com.bandhan.enums.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    List<Booking> findByBookingStatus(BookingStatus bookingStatus);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByEventId(Long eventId);
}
