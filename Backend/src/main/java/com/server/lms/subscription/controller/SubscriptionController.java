package com.server.lms.subscription.controller;

import com.server.lms._shared.dto.ApiResponse;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.payment.dto.request.PaymentInitiateRequest;
import com.server.lms.payment.dto.response.PaymentInitiateResponse;
import com.server.lms.subscription.dto.request.SubscriptionRequest;
import com.server.lms.subscription.dto.response.SubscriptionResponse;
import com.server.lms.subscription.repository.SubscriptionRepository;
import com.server.lms.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscripe")
    public ResponseEntity<ApiResponse<?>> subscribe(
            @RequestBody @Valid SubscriptionRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<PaymentInitiateResponse>builder()
                                .success(true)
                                .message("Subscription Created Successfully")
                                .data(subscriptionService.subscribe(dto))
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllSubscriptions() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PageResponse<SubscriptionResponse>>builder()
                                .success(true)
                                .message("Subscriptions Retrieved Successfully")
                                .data(subscriptionService.getAllSubscriptions())
                                .build()
                );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(
            @PathVariable @NotBlank @NotNull String id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<SubscriptionResponse>builder()
                                .success(true)
                                .message("Subscription Retrieved Successfully")
                                .data(subscriptionService.getById(id))
                                .build()
                );
    }


    @PostMapping("/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deactivateExpiredSubscriptions() {
        subscriptionService.deactivateExpiredSubscriptions();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<Void>builder()
                                .success(true)
                                .message("Deactivation Completed Successfully")
                                .data(null)
                                .build()
                );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<?>> getUserActiveSubscription(
            @RequestParam @NotBlank @NotNull String userId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<SubscriptionResponse>builder()
                                .success(true)
                                .message("Active Subscription Retrieved Successfully")
                                .data(subscriptionService.getUserActiveSubscription(userId))
                                .build()
                );
    }

    @PostMapping("/{subscriptionId}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelSubscription(
            @PathVariable @NotBlank @NotNull String subscriptionId,
            @RequestParam @NotBlank @NotNull String reason
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<SubscriptionResponse>builder()
                                .success(true)
                                .message("Active Subscriptions Retrieved Successfully")
                                .data(subscriptionService.cancelSubscription(subscriptionId, reason))
                                .build()
                );
    }

    @PostMapping("/{subscriptionId}/activate")
    public ResponseEntity<ApiResponse<?>> activateSubscription(
            @PathVariable @NotBlank @NotNull String subscriptionId,
            @RequestParam @NotBlank @NotNull String paymentId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<SubscriptionResponse>builder()
                                .success(true)
                                .message("Subscription Activated Successfully")
                                .data(subscriptionService.activateSubscription(subscriptionId, paymentId))
                                .build()
                );
    }

}
