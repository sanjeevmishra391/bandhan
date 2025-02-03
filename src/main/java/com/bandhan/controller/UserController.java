package com.bandhan.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandhan.entity.User;
import com.bandhan.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsers(
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String mobile,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String address
        ) {
        
        return ResponseEntity.ok(userService.searchUsers(id, firstName, lastName, mobile, email, address));
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @PutMapping("/cust/{id}")
    public ResponseEntity<Map<String, Object>> updateUserById(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUserById(id, user));
    }

    @DeleteMapping("/cust/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
}
