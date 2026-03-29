package com.server.lms._shared.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplate {
    RESET_PASSWORD("reset-password");
    private final String templateName;
}
