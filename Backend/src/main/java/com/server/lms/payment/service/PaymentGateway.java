package com.server.lms.payment.service;

import com.server.lms.payment.dto.response.PaymentLinkResponse;
import com.server.lms.payment.entity.Payment;
import com.server.lms.payment.enums.PaymentType;
import com.server.lms.user.entity.User;

import java.util.HashMap;
import java.util.Map;

public interface PaymentGateway {
    PaymentLinkResponse createPaymentLink(User user, Payment payment);

    Map<String, String> /* Metadata & isValid field*/ validatePayment(String paymentId);


    default Map<String, String> buildMetadata(User user, Payment payment, String baseUrl) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("user_id", user.getId());
        metadata.put("user_email", user.getEmail());
        metadata.put("user_sms", user.getPhone());
        metadata.put("payment_id", payment.getId());
        metadata.put("amount", String.valueOf(payment.getAmount() * 100 /* cents */));
        metadata.put("reminder_enable", "true");

        String successUrl = baseUrl + "/payment-success" + payment.getId();

        metadata.put("callback_url", successUrl);
        metadata.put("callback_method", "get");

        if (payment.getPaymentType() == PaymentType.MEMBERSHIP) {
            metadata.put("subscription_id", String.valueOf(payment.getSubscription().getId()));
            metadata.put("subscription_plan_code", payment.getSubscription().getSubscriptionPlan().getPlanCode());
            metadata.put("payment_type", PaymentType.MEMBERSHIP.name());
        } else if (payment.getPaymentType() == PaymentType.PENALTY) {
            metadata.put("penalty_id", String.valueOf(payment.getPenalty().getId()));
            metadata.put("payment_type", PaymentType.PENALTY.name());
        }
        return metadata;
    }
}
