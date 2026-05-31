package com.server.lms.payment.dto.request;

import com.server.lms.payment.enums.PaymentProvider;
import com.server.lms.payment.enums.PaymentStatus;
import com.server.lms.payment.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String paymentId;

    private String userId;

    private String userName;

    private String userEmail;

    private String BookLoanId;

    private String subscriptionId;

    private String subscriptionPlanCode;

    private PaymentType paymentType;

    private PaymentStatus status;

    private PaymentProvider paymentProvider;

    private Long amount;

    private String transactionId;

    private String gatewayPaymentOrderId;

    private String gatewaySignature;

    private Integer retryCount;

    private String description;

    private String failureReason;
}
