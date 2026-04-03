package com.server.lms.subscription.dto.response;

import com.server.lms.user.dto.response.UserResponse;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SubscriptionResponse {
    private String id;

    private UserResponse user;

    private SubscriptionPlanResponse subscriptionPlan;

    private String planName;

    private String planCode;

    private Long price;

    private Integer maxBooksAllowed;

    private Integer maxDaysOfBook;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isActive;

    private Boolean autoRenew;

    private LocalDateTime cancelledAt;

    private String cancelledReason;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
