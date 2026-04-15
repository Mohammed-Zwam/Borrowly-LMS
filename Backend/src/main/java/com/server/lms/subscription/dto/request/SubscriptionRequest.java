package com.server.lms.subscription.dto.request;

import com.server.lms.payment.enums.PaymentProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubscriptionRequest {
    @NotNull(message = "Subscription plan ID is required")
    @NotBlank(message = "Subscription plan ID is required")
    private String subscriptionPlanId;

    private Boolean autoRenew;

    private PaymentProvider paymentProvider;

    private String notes;
}
