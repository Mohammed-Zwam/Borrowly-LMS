package com.server.lms.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRequest {
    @NotNull(message = "Book ID is mandatory")
    private String bookId;

    private String notes;
}
