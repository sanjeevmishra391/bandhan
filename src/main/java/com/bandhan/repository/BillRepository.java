package com.bandhan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bandhan.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
   
}