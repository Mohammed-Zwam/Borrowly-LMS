package com.server.lms._shared.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;


    @Override
    public void sendEmail(String to, String subject, EmailTemplate templateName, Map<String, Object> variables) {

        try {
            Context context = new Context();
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            context.setVariables(variables);

            String htmlBody = templateEngine.process(templateName.getTemplateName(), context);


            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            javaMailSender.send(mimeMessage);
        } catch (
                MailException ex
        ) {
            throw new MailSendException("Failed to send email");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
