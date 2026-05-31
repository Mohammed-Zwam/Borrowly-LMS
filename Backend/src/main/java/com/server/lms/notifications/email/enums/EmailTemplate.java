package com.server.lms.notifications.email.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplate {
    RESET_PASSWORD("reset-password"),
    MEMBERSHIP_PAYMENT_SUCCESS("membership-payment-success");

    private final String templateName;
}
