package com.bandhan.repository;

import com.bandhan.entity.User;
import com.bandhan.enums.UserStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>  {
    List<User> findByUserStatus(UserStatus userStatus);
    Optional<User> findByMobile(String mobile);
    Boolean existsByMobile(String mobile);
    Boolean existsByEmail(String email);
}