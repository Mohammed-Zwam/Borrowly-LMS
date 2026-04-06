package com.server.lms.subscription.entity;

import com.server.lms.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne // User can have multiple subscriptions in DB (Expired, Active, Canceled) BUT ONLY ONE SUBSCRIPTION CAN BE ACTIVE AT A TIME
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    private SubscriptionPlan subscriptionPlan;

    private String planName;

    private String planCode;

    // updating a plan details does not affect already active subscriptions (already subscribed plans in the past <old details>).
    private Long price;

    @Column(nullable = false)
    private Integer maxBooksAllowed;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isActive;

    private Boolean autoRenew;

    private LocalDateTime cancelledAt;

    private String cancelledReason;

    private Integer maxRenewals = 0;

    private Integer maxBorrowingDays = 7;

    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public boolean isValid() {
        if (!isActive) return false;
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }


    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }

    private long getDaysRemaining() {
        if (isExpired()) {
            return 0;
        }
        return endDate.toEpochDay() - LocalDate.now().toEpochDay();
    }

    public void calculateEndDate() {
        if (subscriptionPlan != null && startDate != null)
            endDate = startDate.plusDays(subscriptionPlan.getDurationDays());
    }

    public void initFromPlan() {
        if (subscriptionPlan != null) {
            planName = subscriptionPlan.getName();
            planCode = subscriptionPlan.getPlanCode();
            price = subscriptionPlan.getPrice();
            maxBooksAllowed = subscriptionPlan.getMaxBookAllowed();
            maxRenewals = subscriptionPlan.getMaxRenewals();
            maxBorrowingDays =  subscriptionPlan.getMaxBorrowingDays();
            if (startDate == null) this.startDate = LocalDate.now();
            calculateEndDate();
        }
    }
}
