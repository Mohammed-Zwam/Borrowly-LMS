package com.server.lms.reservation.dto.request;

import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms.reservation.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSearchRequest extends PageRequestDTO {
    private String userId;
    private String bookId;
    private ReservationStatus reservationStatus;
    private Boolean isActiveOnly;
}
