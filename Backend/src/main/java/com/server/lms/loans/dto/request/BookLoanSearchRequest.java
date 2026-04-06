package com.server.lms.loans.dto.request;

import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms.loans.enums.BookLoanState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookLoanSearchRequest extends PageRequestDTO {
    private String userId;
    private String bookId;
    private BookLoanState bookLoanState;
    private boolean overdueOnly;
    private boolean unpaidFinesOnly;
    private LocalDate startDate;
    private LocalDate endDate;
}
