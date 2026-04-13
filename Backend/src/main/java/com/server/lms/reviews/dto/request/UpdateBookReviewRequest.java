package com.server.lms.reviews.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateBookReviewRequest {
    @NotNull(message = "Rating is mandatory")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    private String reviewDescription;

    @Size(min = 5, max = 50, message = "Review must be between 10 and 50 characters")
    private String title;
}
