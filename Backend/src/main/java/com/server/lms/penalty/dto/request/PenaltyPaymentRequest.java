package com.server.lms.penalty.dto.request;

import com.server.lms.payment.enums.PaymentProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PenaltyPaymentRequest {
    @NotNull(message = "Transaction ID is required")
    private String transactionId;

    @NotNull(message = "Payment Provider is required")
    private PaymentProvider paymentProvider;
}
