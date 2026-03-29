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
public class RestPasswordRequest {
    @NotNull(message = "token is required")
    @NotBlank(message = "token is required")
    private String token;

    @NotNull(message = "new password is required")
    @NotBlank(message = "new password is required")
    private String password;
}
