package com.server.lms.subscription.controller;


import com.server.lms._shared.dto.ApiResponse;
import com.server.lms.subscription.dto.request.SubscriptionPlanRequest;
import com.server.lms.subscription.dto.response.SubscriptionPlanResponse;
import com.server.lms.subscription.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(
            @PathVariable String id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<SubscriptionPlanResponse>builder()
                                .success(true)
                                .message("Subscription Plan Retrieved Successfully")
                                .data(subscriptionPlanService.getById(id))
                                .build()
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<SubscriptionPlanResponse>>builder()
                                .success(true)
                                .message("Subscription Plans Retrieved Successfully")
                                .data(subscriptionPlanService.getAll())
                                .build()
                );
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> create(
            @RequestBody @Valid SubscriptionPlanRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<SubscriptionPlanResponse>builder()
                                .success(true)
                                .message("Subscription Plan Created Successfully")
                                .data(subscriptionPlanService.create(dto))
                                .build()
                );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable String id,
            @RequestBody @Valid SubscriptionPlanRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<SubscriptionPlanResponse>builder()
                                .success(true)
                                .message("Subscription Plan Updated Successfully")
                                .data(subscriptionPlanService.update(id, dto))
                                .build()
                );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable String id
    ) {
        subscriptionPlanService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(
                        ApiResponse.<Void>builder()
                                .success(true)
                                .message("Subscription Plan Deleted Successfully")
                                .data(null)
                                .build()
                );
    }
}
