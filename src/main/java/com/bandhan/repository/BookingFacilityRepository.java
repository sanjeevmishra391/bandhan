package com.bandhan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bandhan.entity.BookingFacility;
import com.bandhan.entity.BookingFacilityId;

public interface BookingFacilityRepository extends JpaRepository<BookingFacility, BookingFacilityId> {
    
}
