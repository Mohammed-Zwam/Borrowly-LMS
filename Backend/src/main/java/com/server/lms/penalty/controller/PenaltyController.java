package com.server.lms.penalty.controller;

import com.server.lms._shared.dto.ApiResponse;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.payment.dto.response.PaymentInitiateResponse;
import com.server.lms.penalty.dto.request.PenaltyCancellationRequest;
import com.server.lms.penalty.dto.request.PenaltyPaymentRequest;
import com.server.lms.penalty.dto.request.PenaltyRequest;
import com.server.lms.penalty.dto.response.PenaltyResponse;
import com.server.lms.penalty.enums.PenaltyState;
import com.server.lms.penalty.enums.PenaltyType;
import com.server.lms.penalty.service.PenaltyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/penalties")
@RequiredArgsConstructor
public class PenaltyController {
    private final PenaltyService penaltyService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPenalty(
            @RequestBody @Valid PenaltyRequest penaltyRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PenaltyResponse>builder()
                                .success(true)
                                .message("Penalty created successfully")
                                .data(penaltyService.create(penaltyRequest))
                                .build()
                );
    }

    @PostMapping("{penaltyId}/pay")
    public ResponseEntity<ApiResponse<?>> payPenalty(
            @PathVariable @Valid String penaltyId,
            @RequestBody @Valid PenaltyPaymentRequest penaltyPaymentRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PaymentInitiateResponse>builder()
                                .success(true)
                                .message("Payment initiated successfully")
                                .data(penaltyService.payPenalty(penaltyId, penaltyPaymentRequest))
                                .build()
                );
    }

    @PostMapping("{penaltyId}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> cancelPenalty(
            @PathVariable @Valid String penaltyId,
            @RequestParam @Valid String reason
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PenaltyResponse>builder()
                                .success(true)
                                .message("Penalty cancelled successfully")
                                .data(penaltyService.cancelPenalty(penaltyId, reason))
                                .build()
                );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> findAllPenaltiesForUser(
            @RequestParam(required = false) PenaltyState penaltyState,
            @RequestParam(required = false) PenaltyType penaltyType
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<PenaltyResponse>>builder()
                                .success(true)
                                .message("penalties retrieved successfully")
                                .data(penaltyService.findAllPenaltiesForUser(penaltyState, penaltyType))
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> findAllPenalties(
            @RequestParam(required = false) PenaltyState penaltyState,
            @RequestParam(required = false) PenaltyType penaltyType,
            @ParameterObject @ModelAttribute PageRequestDTO pageRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PageResponse<PenaltyResponse>>builder()
                                .success(true)
                                .message("Penalties retrieved successfully")
                                .data(penaltyService.findAllPenalties(penaltyState, penaltyType, pageRequest))
                                .build()
                );
    }
}
