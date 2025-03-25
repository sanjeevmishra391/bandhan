package com.bandhan.dto;

import java.sql.Timestamp;

import com.bandhan.enums.UserRole;
import com.bandhan.enums.UserStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignupRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number. Must be 10 digits.")
    private String mobile;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    private String address;

    private UserRole userRole = UserRole.CUSTOMER;

    private UserStatus userStatus = UserStatus.ACTIVE;

    private Timestamp creationDate;

    private Timestamp lastUpdateDate;
}
