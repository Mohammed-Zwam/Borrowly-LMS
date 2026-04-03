package com.server.lms.user.controller;

import com.server.lms._shared.dto.ApiResponse;
import com.server.lms.user.dto.response.UserResponse;
import com.server.lms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<UserResponse>>builder()
                                .success(true)
                                .message("Users retrieved successfully")
                                .data(userService.getAllUsers())
                                .build()
                );
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getCurrentUser() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<UserResponse>builder()
                                .success(true)
                                .message("Users retrieved successfully")
                                .data(userService.getUserProfile())
                                .build()
                );
    }
}
