package com.server.lms.penalty.dto.request;

import com.server.lms.penalty.enums.PenaltyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PenaltyRequest {

    @NotNull(message = "Book Loan ID is required")
    private String bookLoanId;

    @NotNull(message = "Penalty Type is required")
    private PenaltyType penaltyType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive value")
    private Long amount;

    private String reason;

    private String notes;

}
