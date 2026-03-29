package com.server.lms.user.service;

import com.server.lms.user.dto.request.UserRequest;
import com.server.lms.user.dto.response.UserResponse;
import com.server.lms.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    User create(UserRequest dto);

    UserResponse getCurrentUser();

    List<UserResponse> getAllUsers();

    User update(User user);

    void updateLastLogin(String userId, LocalDateTime lastLogin);

    User findEntityByEmail(String email);
}
