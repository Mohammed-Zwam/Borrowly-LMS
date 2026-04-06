package com.server.lms.payment.controller;

import com.server.lms._shared.dto.ApiResponse;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.payment.dto.response.PaymentResponse;
import com.server.lms.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllSubscriptions(
            @ParameterObject /* Spring Doc (Scalar / OpenAPI) */ @ModelAttribute PageRequestDTO pageRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PageResponse<PaymentResponse>>builder()
                                .success(true)
                                .message("Payments Retrieved Successfully")
                                .data(paymentService.getAllPayments(pageRequestDTO))
                                .build()
                );
    }

}
