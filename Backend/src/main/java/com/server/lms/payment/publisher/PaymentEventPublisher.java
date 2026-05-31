package com.server.lms.payment.publisher;

import com.server.lms.payment.event.MembershipPaymentSucceededEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishMembershipPaymentSuccessfulEvent(MembershipPaymentSucceededEvent payment) {
        applicationEventPublisher.publishEvent(payment);
    }

}
