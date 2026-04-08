package com.server.lms.payment.dto.request;

import com.server.lms.payment.enums.PaymentProvider;
import com.server.lms.payment.enums.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInitiateRequest {
    private String BookLoanId; // For FINE Payments Only

    @NotNull(message = "User ID is required")
    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotNull(message = "Payment Type is required")
    private PaymentType paymentType;

    @NotNull(message = "Payment Gateway is required")
    @NotBlank

    private PaymentProvider paymentProvider;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive number")
    private Long amount;

    @Size(max = 500, message = "Description can be at most 500 characters")
    private String description;

    private String penaltyId;

    private String subscriptionId;

}
