package com.server.lms._shared.email;

import java.util.Map;

public interface EmailService {
    void sendEmail(String to, String subject, EmailTemplate templateName, Map<String, Object> variables);

}
