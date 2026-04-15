package com.server.lms.reservation.service;

import com.server.lms._shared.base.BaseService;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.reservation.dto.request.ReservationRequest;
import com.server.lms.reservation.dto.request.ReservationSearchRequest;
import com.server.lms.reservation.dto.response.ReservationResponse;
import com.server.lms.reservation.entity.Reservation;

public interface ReservationService extends BaseService<Reservation, String> {
    ReservationResponse create(ReservationRequest reservationRequest);

    ReservationResponse create(ReservationRequest reservationRequest, String userId);

    ReservationResponse cancelReservation(String reservationId);

    ReservationResponse completeReservation(String reservationId);

    PageResponse<ReservationResponse> findMyReservations(ReservationSearchRequest searchRequest);

    PageResponse<ReservationResponse> findAllReservations(ReservationSearchRequest searchRequest);

}
