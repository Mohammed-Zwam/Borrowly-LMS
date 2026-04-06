package com.server.lms.payment.dto.response;

import com.server.lms.payment.enums.PaymentProvider;
import com.server.lms.payment.enums.PaymentStatus;
import com.server.lms.payment.enums.PaymentType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private String id;

    private String userId;

    private String userName;

    private String userEmail;

    private String BookLoanId;

    private String subscriptionId;

    private PaymentType paymentType;

    private PaymentStatus paymentStatus;

    private PaymentProvider paymentProvider;

    private Long amount;

    private String transactionId;

    private String gatewayPaymentOrderId;

    private String gatewaySignature;

    private String description;

    private String failureReason;

    private LocalDateTime initiatedAt;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
