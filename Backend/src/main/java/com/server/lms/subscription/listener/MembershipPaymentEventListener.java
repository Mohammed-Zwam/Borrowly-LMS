package com.server.lms.subscription.listener;

import com.server.lms.payment.event.MembershipPaymentSucceededEvent;
import com.server.lms.subscription.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MembershipPaymentEventListener {
    private final SubscriptionService subscriptionService;

    @Async
    @EventListener
    @Transactional
    public void handleMembershipPaymentSucceededEvent(MembershipPaymentSucceededEvent payment) {
        subscriptionService.activateSubscription(payment.getSubscriptionId(), payment.getPaymentId());
        log.info("Activated subscription with ID {} for payment ID {}", payment.getSubscriptionId(), payment.getPaymentId());
    }
}
