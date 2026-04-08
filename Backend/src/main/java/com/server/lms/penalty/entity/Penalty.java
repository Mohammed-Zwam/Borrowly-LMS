package com.server.lms.penalty.entity;

import com.server.lms.loans.entity.BookLoan;
import com.server.lms.penalty.enums.PenaltyState;
import com.server.lms.penalty.enums.PenaltyType;
import com.server.lms.penalty.exception.PenaltyException;
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
@Table(name = "penalties")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Penalty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BookLoan bookLoan; // ex: delay & damage

    @ManyToOne
    private User cancelledBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private User paidBy;

    @Enumerated(EnumType.STRING)
    private PenaltyType penaltyType;

    @Enumerated(EnumType.STRING)
    private PenaltyState penaltyState;

    @Column(nullable = false)
    private Long amount;

    private Long partialAmountPaid = 0L;

    @Column(length = 500)
    private String reason;

    @Column(length = 1000)
    private String notes;

    private LocalDateTime paidAt;

    private LocalDateTime cancelledAt;

    @Column(length = 500)
    private String cancellationReason;


    @Column(length = 100)
    private String transactionId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void handlePenalityPayment(long paymentAmount) {
        if (paymentAmount <= 0) throw new PenaltyException("Payment amount must be positive");

        this.partialAmountPaid = this.partialAmountPaid + paymentAmount;

        if (this.partialAmountPaid >= this.amount) {
            this.penaltyState = PenaltyState.PAID;
            this.paidAt = LocalDateTime.now();
        } else {
            this.penaltyState = PenaltyState.PARTIALLY_PAID;

        }
    }
}
