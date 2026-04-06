package com.server.lms.subscription.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class SubscriptionPlanRequest {

    @NotNull(message = "Plan code is required")
    @NotBlank(message = "Plan code is required")
    private String planCode;

    @NotNull(message = "Name is required ")
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Name must be less than 100 characters")
    private String description;

    @NotNull(message = "Duration days is required")
    private Integer durationDays;

    @NotNull(message = "Price is required")
    private Long price;

    private String currency = "EGP";

    @NotNull(message = "Max books is required")
    @Positive(message = "Max books must be positive")
    private Integer maxBookAllowed;

    @Min(value = 0, message = "Min renewals must be positive")
    @Max(value = 5, message = "Max renewals must be less than 10")
    private Integer maxRenewals = 0;

    @Min(value = 7, message = "Min renewals must be positive")
    @Max(value = 30, message = "Max renewals must be less than 10")
    private Integer maxBorrowingDays = 7;

    @Positive(message = "Display order must be positive")
    private Integer displayOrder = 0;

    private Boolean isActive = true;
    private Boolean isFeatured = false;

    private String badgeText;

    private String adminNotes;

}
