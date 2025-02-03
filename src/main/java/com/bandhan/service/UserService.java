package com.bandhan.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bandhan.entity.User;
import com.bandhan.enums.UserStatus;
import com.bandhan.exception.BadRequestException;
import com.bandhan.exception.ResourceNotFoundException;
import com.bandhan.repository.UserRepository;
import com.bandhan.specification.UserSpecification;
import com.bandhan.utils.UserUtils;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getUserList() {
        List<User> users = userRepository.findByUserStatus(UserStatus.ACTIVE);
        return Map.of("message", "User list",
                "result",  UserUtils.sanitizeUser(users));
    }

    public Map<String, Object> searchUsers(Long id, String firstName, String lastName, String mobile, String email, String address) {

        Specification<User> spec = Specification.where(UserSpecification.hasStatus(UserStatus.ACTIVE))
                                                .and(UserSpecification.hasId(id))
                                                .and(UserSpecification.firstNameContains(firstName))
                                                .and(UserSpecification.lastNameContains(lastName))
                                                .and(UserSpecification.hasMobile(mobile))
                                                .and(UserSpecification.addressContains(address))
                                                .and(UserSpecification.hasEmail(email));

        List<User> users = userRepository.findAll(spec);

        return Map.of("message", "Search result",
            "result", UserUtils.sanitizeUser(users));
    }

    public Map<String, Object> updateUserById(Long id, User user) {
        // check if id exists
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    
        // Check if the user is active
        if (existingUser.getUserStatus() != UserStatus.ACTIVE) {
            throw new BadRequestException("User with id " + id + " is not active and cannot be updated.");
        }
    
        // Update user details
        Optional.ofNullable(user.getFirstName()).ifPresent(existingUser::setFirstName);
        Optional.ofNullable(user.getLastName()).ifPresent(existingUser::setLastName);
        Optional.ofNullable(user.getMobile()).ifPresent(existingUser::setMobile);
        Optional.ofNullable(user.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(user.getAddress()).ifPresent(existingUser::setAddress);
    
        User updatedUser = userRepository.save(existingUser);
    
        return Map.of(
            "message", "User with id " + id + " has been updated successfully",
            "result",  UserUtils.sanitizeUser(updatedUser)
        );
    }
    
    public Map<String, Object> deleteUserById(long id) {
        User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        user.setUserStatus(UserStatus.DEACTIVATED);
        userRepository.save(user);

        return Map.of("message", "User with id " + id + " has been deactivated successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        return userRepository.findByMobile(mobile)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}