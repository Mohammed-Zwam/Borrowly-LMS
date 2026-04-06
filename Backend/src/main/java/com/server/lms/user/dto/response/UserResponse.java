package com.server.lms.user.dto.response;

import com.server.lms.user.enums.UserRole;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String id;
    private String email;
    private String password;
    private String phone;
    private String name;
    private UserRole role;
    private String username;
    private LocalDateTime lastLogin;
}
