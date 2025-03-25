package com.bandhan.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.bandhan.dto.UserResponse;
import com.bandhan.entity.User;

public class UserUtils {

    public static List<UserResponse> userResponse(List<User> users) {
        return users.stream().map(user -> {
            return new UserResponse(user);
        }).collect(Collectors.toList());
    }

    public static UserResponse userResponse(User user) {
        return new UserResponse(user);
    }
}
