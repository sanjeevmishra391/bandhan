package com.bandhan.repository;

import com.bandhan.entity.Facility;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByNameContainingIgnoreCase(String name);
    Optional<Facility> findByName(String name);
    Optional<Facility> findByNameIgnoreCase(String name);
}
