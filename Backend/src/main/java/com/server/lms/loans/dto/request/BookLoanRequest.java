package com.server.lms.loans.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookLoanRequest {
    @NotNull(message = "Book ID is mandatory")
    private String bookId;

    @Min(value = 1, message = "Checkout days must be at least 1")
    private Integer borrowingDays = 14; // Default: 14 days

    private String notes;
}
