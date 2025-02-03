package com.bandhan.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.bandhan.entity.User;

public class UserUtils {
    public static User sanitizeUser(User user) {
        User sanitizedUser = new User(user.getId(), user.getFirstName(), user.getLastName(), user.getAddress(), user.getMobile(), user.getEmail(), null, user.getUserRole(), null, user.getCreationDate(), user.getLastUpdateDate());
        return sanitizedUser;
    }

    public static List<User> sanitizeUser(List<User> users) {
        return users.stream().map(user -> { 
            User sanitizedUser = new User(user.getId(), user.getFirstName(), user.getLastName(), user.getAddress(), user.getMobile(), user.getEmail(), null, user.getUserRole(), null, user.getCreationDate(), user.getLastUpdateDate());
            return sanitizedUser; }).collect(Collectors.toList());
    }
}
