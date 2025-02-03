package com.bandhan.controller;

import com.bandhan.entity.User;
import com.bandhan.exception.BadCredentialsException;
import com.bandhan.exception.BadRequestException;
import com.bandhan.exception.ResourceNotFoundException;
import com.bandhan.service.AuthService;
import com.bandhan.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody User user) {
        return ResponseEntity.ok(authService.signup(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
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

        String jwtToken = authService.generateToken(userDetails);
        
        return ResponseEntity.ok(
            Map.of("message", "Generated token for current user",
            "result", Map.of("token", jwtToken, "user", userDetails)));
    }
}
