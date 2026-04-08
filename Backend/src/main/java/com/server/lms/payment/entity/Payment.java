package com.server.lms.payment.entity;

import com.server.lms.payment.enums.PaymentProvider;
import com.server.lms.payment.enums.PaymentStatus;
import com.server.lms.payment.enums.PaymentType;
import com.server.lms.penalty.entity.Penalty;
import com.server.lms.subscription.entity.Subscription;
import com.server.lms.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Subscription subscription;

    @ManyToOne
    private Penalty penalty;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentProvider paymentProvider;

    private String transactionId;

    private String gatewayPaymentOrderId;

    private String gatewaySignature;

    private String description;

    private String failureReason;

    private Long amount;

    @CreationTimestamp
    private LocalDateTime initiatedAt;

    private LocalDateTime completedAt; // when success

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

