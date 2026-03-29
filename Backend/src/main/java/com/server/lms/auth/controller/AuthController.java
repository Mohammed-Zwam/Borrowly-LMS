package com.server.lms.auth.controller;

import com.server.lms._shared.dto.ApiResponse;
import com.server.lms.auth.dto.request.ForgetPasswordRequest;
import com.server.lms.auth.dto.request.LoginRequest;
import com.server.lms.auth.dto.request.RestPasswordRequest;
import com.server.lms.auth.dto.response.AuthResponse;
import com.server.lms.auth.service.AuthService;
import com.server.lms.user.dto.request.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(
            @RequestBody @Valid UserRequest userDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<AuthResponse>builder()
                                .success(true)
                                .message("User Registered Successfully")
                                .data(authService.signup(userDTO))
                                .build()
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody @Valid LoginRequest loginRequest
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<AuthResponse>builder()
                                .success(true)
                                .message("User Logged In Successfully")
                                .data(authService.login(loginRequest))
                                .build()
                );
    }


    @PostMapping("/forget-password")
    public ResponseEntity<ApiResponse<?>> forgetPassword(
            @RequestBody @Valid ForgetPasswordRequest forgetPasswordRequest
    ) {
        authService.createPasswordResetToken(forgetPasswordRequest.getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<String>builder()
                                .success(true)
                                .message("Rest Token Sent Successfully, Check Your Email ^-^")
                                .data("CHECK EMAIL")
                                .build()
                );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(
            @RequestBody @Valid RestPasswordRequest restPasswordRequest
    ) {
        authService.resetPassword(restPasswordRequest.getToken(), restPasswordRequest.getPassword());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<String>builder()
                                .success(true)
                                .message("Password Reset Successfully")
                                .data("DONE")
                                .build()
                );
    }
}
