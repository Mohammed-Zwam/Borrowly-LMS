package com.server.lms.notifications.email.service;

import com.server.lms.notifications.email.enums.EmailTemplate;

import java.util.Map;

public interface EmailService {
    void sendEmail(String to, String subject, EmailTemplate templateName, Map<String, Object> variables);

}
