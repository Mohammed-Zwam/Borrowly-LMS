package com.server.lms.payment.dto.response;

import com.server.lms.payment.enums.PaymentProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitiateResponse {
    private String id;

    private PaymentProvider paymentProvider;

    private String transactionId;

    private String gatewayPaymentOrderId;

    private String subscriptionId;

    private Long amount;

    private String description;

    private String checkoutUrl; // redirect to this url to complete payment
}
