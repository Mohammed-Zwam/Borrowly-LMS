package com.server.lms.reservation.dto.response;

import com.server.lms.reservation.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private String id;

    private String userId;
    private String userName;
    private String userEmail;

    private String bookId;
    private String bookTitle;
    private String bookIsbn;
    private String bookAuthor;
    private Boolean isBookAvailable;

    private ReservationStatus reservationStatus;
    private LocalDateTime availableAt;
    private LocalDateTime availableUntil;
    private LocalDateTime fulfilledAt;
    private LocalDateTime cancelledAt;
    private Integer queuePosition;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean isExpired;
    private boolean canBeCancelled;
    private Long hoursUntilExpiry;
}
