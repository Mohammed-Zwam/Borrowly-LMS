package com.server.lms.subscription.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "subscription_plans")
@EntityListeners({AuditingEntityListener.class})
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(nullable = false, unique = true)
    private String planCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer durationDays;

    @Column(nullable = false)
    private Long price;


    private String currency = "EGP";

    @Column(nullable = false)
    @Positive(message = "Max books must be positive")
    private Integer maxBookAllowed;


    @Column(nullable = false)
    @Positive(message = "Max days must be positive")
    private Integer maxDaysPerBook;

    @Positive(message = "Display order must be positive")
    private Integer displayOrder = 0;

    private Boolean isActive = true;
    private Boolean isFeatured = false;

    private String badgeText;

    private String adminNotes;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Auditing
    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}
