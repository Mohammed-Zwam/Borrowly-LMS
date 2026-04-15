package com.server.lms.payment.event.listener;

import com.server.lms.payment.entity.Payment;
import com.server.lms.subscription.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final SubscriptionService subscriptionService;

    @Async
    @EventListener
    @Transactional
    public void handlePaymentSuccess(Payment payment) {
        switch (payment.getPaymentType()) {
            case MEMBERSHIP:
                subscriptionService.activateSubscription(payment.getSubscription().getId(), payment.getId());
            case PENALTY:
            case LOST_BOOK_PENALITY:
            case DAMAGED_BOOK_PENALITY:
                break;

        }
    }
}
