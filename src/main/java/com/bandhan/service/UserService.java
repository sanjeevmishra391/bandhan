package com.bandhan.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bandhan.entity.User;
import com.bandhan.enums.UserRole;
import com.bandhan.enums.UserStatus;
import com.bandhan.exception.BadRequestException;
import com.bandhan.exception.ResourceNotFoundException;
import com.bandhan.repository.UserRepository;
import com.bandhan.specification.UserSpecification;
import com.bandhan.utils.UserUtils;
import com.bandhan.utils.Util;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getUserList() {
        List<User> users = userRepository.findByUserStatus(UserStatus.ACTIVE);
        return Util.response("User list", UserUtils.userResponse(users), "success");
    }

    public Map<String, Object> getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User id is not valid"));
        return Util.response("User details with user id " + id, user, "success");
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
        return Util.response("Search result", UserUtils.userResponse(users), "success");
    }

    public Map<String, Object> updateUserById(Long id, User user) {
        // check if id exists
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        User loggedUser = getAuthenticatedUser();
            
        // if current user is ADMIN > allow updating anyone's data
        // if current user is CUSTOMER > self data can only be updated
        if(loggedUser.getUserRole().equals(UserRole.CUSTOMER) && loggedUser.getId() != existingUser.getId()) {
            throw new BadRequestException("The current user with id " +  loggedUser.getId() + " cannot update other user's data");
        }
    
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

        return Util.response("User with id " + id + " has been updated successfully", UserUtils.userResponse(updatedUser), "success");
    }
    
    public Map<String, Object> deleteUserById(long id) {
        User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        User loggedUser = getAuthenticatedUser();

        // if current user is ADMIN > allow deleting anyone's data
        // if current user is CUSTOMER > self data can only be deleted
        if(loggedUser.getUserRole().equals(UserRole.CUSTOMER) && loggedUser.getId() != user.getId()) {
            throw new BadRequestException("The current user with id " + loggedUser.getId() + " cannot delete other user's data");
        }

        if(user.getUserStatus() == UserStatus.DEACTIVATED) {
            throw new BadRequestException("The user with id " + user.getId() + " is already deactivated");
        }

        user.setUserStatus(UserStatus.DEACTIVATED);
        userRepository.save(user);

        return Util.response("User with id " + id + " has been deactivated successfully", "success");
    }

    public User getAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) authentication.getPrincipal();
        return userRepository.findById(user.getId()).get();
    }

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        return userRepository.findByMobile(mobile)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}