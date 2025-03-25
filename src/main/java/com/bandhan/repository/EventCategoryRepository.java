package com.bandhan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandhan.entity.EventCategory;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    List<EventCategory> findByNameContainingIgnoreCase(String name);
    Boolean existsByName(String name);
}
