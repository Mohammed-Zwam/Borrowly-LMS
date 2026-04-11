package com.server.lms.reservation.controller;

import com.server.lms._shared.dto.ApiResponse;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.reservation.dto.request.ReservationRequest;
import com.server.lms.reservation.dto.request.ReservationSearchRequest;
import com.server.lms.reservation.dto.response.ReservationResponse;
import com.server.lms.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;


    @PostMapping
    public ResponseEntity<ApiResponse<?>> createReservation(
            @RequestBody @Valid ReservationRequest reservationRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ReservationResponse>builder()
                                .success(true)
                                .message("Reservation created successfully")
                                .data(reservationService.create(reservationRequest))
                                .build()
                );
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createReservationForUser(
            @PathVariable @Valid String userId,
            @RequestBody @Valid ReservationRequest reservationRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ReservationResponse>builder()
                                .success(true)
                                .message("Reservation created successfully")
                                .data(reservationService.create(reservationRequest, userId))
                                .build()
                );
    }


    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelReservation(
            @PathVariable @Valid String reservationId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<ReservationResponse>builder()
                                .success(true)
                                .message("Reservation cancelled successfully")
                                .data(reservationService.cancelReservation(reservationId))
                                .build()
                );
    }

    @PostMapping("/{reservationId}/complete")
    public ResponseEntity<ApiResponse<?>> completeReservation(
            @PathVariable @Valid String reservationId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<ReservationResponse>builder()
                                .success(true)
                                .message("Reservation completed successfully")
                                .data(reservationService.completeReservation(reservationId))
                                .build()
                );
    }


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getMyReservationsWithFilters(
            @ParameterObject @ModelAttribute ReservationSearchRequest searchRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PageResponse<ReservationResponse>>builder()
                                .success(true)
                                .message("Reservations retrieved successfully")
                                .data(reservationService.findMyReservations(searchRequest))
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllReservationsWithFilters(
            @ParameterObject @ModelAttribute ReservationSearchRequest searchRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PageResponse<ReservationResponse>>builder()
                                .success(true)
                                .message("Reservations retrieved successfully")
                                .data(reservationService.findAllReservations(searchRequest))
                                .build()
                );
    }


}
