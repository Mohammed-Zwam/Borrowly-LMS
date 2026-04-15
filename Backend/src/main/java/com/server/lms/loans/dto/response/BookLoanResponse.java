package com.server.lms.loans.dto.response;

import com.server.lms.loans.enums.BookLoanState;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookLoanResponse {
    private String id;
    private String userId;
    private String userName;
    private String userEmail;
    private String bookId;
    private String bookTitle;
    private String bookIsbn;
    private String bookAuthor;
    private String bookCoverImage;
    private BookLoanState bookLoanState;
    private LocalDate borrowingDate;
    private LocalDate dueDate;
    private Long remainingDays;
    private LocalDate returnDate;
    private Integer renewalCount;
    private Integer maxRenewals;
    private BigDecimal fineAmount;
    private Boolean finePaid;
    private String notes;
    private Boolean isOverdue;
    private Integer overdueDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
