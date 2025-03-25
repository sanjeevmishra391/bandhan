package com.bandhan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bandhan.entity.Charge;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
    
}
