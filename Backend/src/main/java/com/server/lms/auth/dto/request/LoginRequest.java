package com.server.lms.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "email or password is required")
    @NotBlank(message = "email or password is required")
    @Email
    private String email;

    @NotNull(message = "email or password is required")
    @NotBlank(message = "email or password is required")
    private String password;
}
