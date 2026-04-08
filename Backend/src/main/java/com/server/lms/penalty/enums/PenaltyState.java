package com.server.lms.penalty.enums;

public enum PenaltyState {
    UNPAID,

    PARTIALLY_PAID, // Part of the penalty has been paid

    PAID,

    CANCELLED // The penalty has been cancelled and does not need to be paid.
}
