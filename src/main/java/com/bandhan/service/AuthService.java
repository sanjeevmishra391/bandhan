package com.bandhan.service;

import com.bandhan.entity.User;
import com.bandhan.exception.BadCredentialsException;
import com.bandhan.exception.BadRequestException;
import com.bandhan.exception.ResourceNotFoundException;
import com.bandhan.repository.UserRepository;
import com.bandhan.utils.UserUtils;
import com.bandhan.utils.Util;
import com.bandhan.config.JwtUtil;
import com.bandhan.dto.UserSignupRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    public Map<String, Object> signup(UserSignupRequest userRequest) {
        // Validations
        // Check if mobile already exists
        if (userRepository.existsByMobile(userRequest.getMobile())) {
            throw new BadRequestException("Mobile number is already registered.");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new BadRequestException("Email is already registered.");
        }

        // Encode the password
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        
        // Save the user to the database
        User newUser = userRepository.save(requestToUser(userRequest));

        return Util.response("User registered successfully", UserUtils.userResponse(newUser), "success");
    }

    public String generateToken(UserDetails userDetails) {
        return jwtUtil.generateToken(userDetails);
    }

    public Map<String, Object> login(User user) {
        // Validate input
        if (user.getMobile() == null || user.getPassword() == null) {
            throw new BadRequestException("Mobile and Password must not be null");
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getMobile(), user.getPassword()));
        } catch(AccountExpiredException ex) {
            throw new ResourceNotFoundException("User is not active");
        } catch(Exception ex) {
            throw new BadCredentialsException("Invalid mobile number or password.");
        }

        // Load user details
        UserDetails userDetails = userService.loadUserByUsername(user.getMobile());

        if (userDetails == null) {
            throw new BadCredentialsException("User not found");
        }

        String jwtToken = generateToken(userDetails);
        
        return
            Map.of("message", "Generated token for current user",
            "result", Map.of("token", jwtToken, "user", userDetails));
    }

    private User requestToUser(UserSignupRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setAddress(userRequest.getAddress());
        user.setMobile(userRequest.getMobile());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setUserRole(userRequest.getUserRole());
        user.setUserStatus(userRequest.getUserStatus());
        return user;
    }
}
