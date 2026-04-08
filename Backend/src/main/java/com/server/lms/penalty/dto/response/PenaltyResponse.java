package com.server.lms.penalty.dto.response;

import com.server.lms.penalty.enums.PenaltyState;
import com.server.lms.penalty.enums.PenaltyType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PenaltyResponse {
    private String id;

    private String bookLoanId;

    private String bookTitle;

    private String bookIsbn;

    private String userId;

    private String userName;

    private String userEmail;

    private PenaltyType penaltyType;

    private PenaltyState penaltyState;

    private Long amount;

    private Long partialAmountPaid;

    private String transactionId;

    private String reason;

    private String notes;

    private String cancellationByUserId;

    private String cancellationByUserName;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    private LocalDateTime paidAt;

    private String paidByUserId;

    private String paidByUserName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
