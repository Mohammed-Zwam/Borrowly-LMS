package com.server.lms.subscription.dto.response;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubscriptionPlanResponse {
    private String id;

    private String planCode;

    private String name;

    private String description;

    private Integer durationDays;

    private Long price;

    private String currency;

    private Integer maxBookAllowed;

    private Integer maxDaysPerBook;

    private Integer displayOrder;

    private Boolean isActive;

    private Boolean isFeatured;

    private String badgeText;

    private String adminNotes;

    private Integer maxRenewals = 0;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Auditing
    private String createdBy;
    private String updatedBy;
}
