package com.server.lms.loans.enums;

public enum BookLoanState {
    RETURNED,
    BORROWED,
    OVERDUE, // deadline missed, not returned
    LOST,
    DAMAGED, // book was damaged during loan period
    RENEWED // book was renewed (re-borrowing)
}
