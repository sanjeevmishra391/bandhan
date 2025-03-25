package com.bandhan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandhan.entity.FacilityProvider;

@Repository
public interface FacilityProviderRepository extends JpaRepository<FacilityProvider, Long> {
    List<FacilityProvider> findByNameContainingIgnoreCase(String name);
    List<FacilityProvider> findByFacilityId(Long facilityId);
}
