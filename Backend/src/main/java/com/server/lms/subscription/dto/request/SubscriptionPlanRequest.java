package com.server.lms.subscription.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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


    @NotNull(message = "Max Days per book is required")
    @Positive(message = "Max days must be positive")
    private Integer maxDaysPerBook;

    @Positive(message = "Display order must be positive")
    private Integer displayOrder = 0;

    private Boolean isActive = true;
    private Boolean isFeatured = false;

    private String badgeText;

    private String adminNotes;

}
