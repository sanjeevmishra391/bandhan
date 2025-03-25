package com.bandhan.dto;

import java.sql.Timestamp;

import com.bandhan.entity.User;
import com.bandhan.enums.UserRole;
import com.bandhan.enums.UserStatus;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String address;
    private UserRole userRole;
    private UserStatus userStatus;
    private Timestamp creationDate;
    private Timestamp lastUpdateDate;

    public UserResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.userRole = user.getUserRole();
        this.userStatus = user.getUserStatus();
        this.creationDate = user.getCreationDate();
        this.lastUpdateDate = user.getLastUpdateDate();
    }
}
