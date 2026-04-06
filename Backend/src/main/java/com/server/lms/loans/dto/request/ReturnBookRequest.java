package com.server.lms.loans.dto.request;

import com.server.lms.loans.enums.BookLoanState;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ReturnBookRequest {
    @NotNull(message = "Book loan ID is mandatory")
    private String bookLoanId;

    private BookLoanState bookLoanState = BookLoanState.RETURNED; // default

    private String notes;
}
