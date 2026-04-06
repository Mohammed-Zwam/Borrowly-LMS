package com.server.lms.loans.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RenewalRequest {
    @NotNull(message = "Book loan ID is mandatory")
    private String bookLoanId;

    @Min(value = 1, message = "Extension days must be at least 1")
    private Integer extensionDays = 14; // Default: extend by 14 days again

    private String notes;
}
