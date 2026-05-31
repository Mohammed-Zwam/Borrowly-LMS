package com.server.lms.notifications.email.listener;

import com.server.lms.notifications.email.enums.EmailTemplate;
import com.server.lms.notifications.email.service.EmailService;
import com.server.lms.payment.event.MembershipPaymentSucceededEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final EmailService emailService;

    @Async
    @EventListener
    @Transactional
    public void handleMembershipPaymentSucceededEvent(MembershipPaymentSucceededEvent payment) {
        emailService.sendEmail(payment.getUserEmail(),
                "Subscription Activation Successful",
                EmailTemplate.MEMBERSHIP_PAYMENT_SUCCESS,
                Map.of(
                        "userName", payment.getUserName(),
                            "subscriptionPlanCode", payment.getSubscriptionPlanCode()
                )
        );

    }
}
