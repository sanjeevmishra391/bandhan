package com.bandhan.service;

import com.bandhan.entity.User;
import com.bandhan.exception.BadRequestException;
import com.bandhan.repository.UserRepository;
import com.bandhan.utils.UserUtils;
import com.bandhan.config.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Map<String, Object> signup(User user) {
        // Validations
        // null checks
        if(user.getFirstName() == null || user.getFirstName().isEmpty())
            throw new BadRequestException("First name can not be null");
        
        if(user.getMobile() == null || user.getMobile().isEmpty())
            throw new BadRequestException("Mobile can not be null");

        if(user.getEmail() == null || user.getEmail().isEmpty())
            throw new BadRequestException("Email can not be null");
        
        // Check if mobile already exists
        if (userRepository.existsByMobile(user.getMobile())) {
            throw new BadRequestException("Mobile number is already registered.");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email is already registered.");
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Save the user to the database
        User newUser = userRepository.save(user);

        return Map.of("message", "User registered successfully",
            "result", UserUtils.sanitizeUser(newUser));
    }

    public String generateToken(UserDetails userDetails) {
        return jwtUtil.generateToken(userDetails);
    }
}
