package com.server.lms.subscription.service;

import com.server.lms._shared.dto.PageResponse;
import com.server.lms._shared.exception.EntityNotFoundException;
import com.server.lms._shared.exception.UnauthorizedException;
import com.server.lms.payment.dto.request.PaymentInitiateRequest;
import com.server.lms.payment.dto.response.PaymentInitiateResponse;
import com.server.lms.payment.enums.PaymentType;
import com.server.lms.payment.service.PaymentService;
import com.server.lms.subscription.dto.request.SubscriptionRequest;
import com.server.lms.subscription.dto.response.SubscriptionResponse;
import com.server.lms.subscription.entity.Subscription;
import com.server.lms.subscription.exception.SubscriptionCancellationException;
import com.server.lms.subscription.mapper.SubscriptionMapper;
import com.server.lms.subscription.repository.SubscriptionRepository;
import com.server.lms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;
    private final PaymentService paymentService;

    @Override
    public SubscriptionResponse getById(String id) {
        return subscriptionMapper.toDTO(
                this.findEntityById(id)
        );
    }

    @Override
    public SubscriptionResponse getUserActiveSubscription(String userId) {
        Subscription subscription = subscriptionRepository.findActiveSubscriptionByUserId(userId, LocalDate.now())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No active subscription found for user with id " + userId
                ));

        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public PageResponse<SubscriptionResponse> getAllSubscriptions() {
        Pageable page = PageRequest.of(0, 10); // TODO: DYNAMIC PAGE SIZE

        Page<Subscription> subscriptions = subscriptionRepository.findAll(page);

        return PageResponse.<SubscriptionResponse>builder()
                .content(
                        subscriptions.getContent()
                                .stream()
                                .map(subscriptionMapper::toDTO)
                                .collect(Collectors.toList())
                )
                .build()
                .setPageInfo(subscriptions);
    }

    @Override
    public PaymentInitiateResponse subscribe(SubscriptionRequest dto) {
        var user = userService.getCurrentUser();

        Subscription subscription = subscriptionMapper.toEntity(dto);

        subscription.initFromPlan();
        subscription.setUser(user);
        subscription.setIsActive(false); // UNTIL PAYMENT PROCESS COMPLETED

        // TODO: PAYMENT VALIDATION

        subscription = subscriptionRepository.save(subscription);

        log.info("User " + user.getEmail() + " subscribed to plan " + subscription.getSubscriptionPlan().getPlanCode());

        var paymentInitiateRequest = PaymentInitiateRequest.builder()
                .amount(subscription.getPrice())
                .description("Subscription for " + subscription.getSubscriptionPlan().getName())
                .userId(user.getId())
                .paymentType(PaymentType.MEMBERSHIP)
                .paymentProvider(dto.getPaymentProvider())
                .subscriptionId(subscription.getId())
                .description("Subscription for " + subscription.getSubscriptionPlan().getName())
                .build();

        return paymentService.initiatePayment(paymentInitiateRequest);
    }


    @Override
    public SubscriptionResponse activateSubscription(String subscriptionId, String paymentId) {
        Subscription subscription = this.findEntityById(subscriptionId);

        if (subscription.getIsActive()) {
            throw new SubscriptionCancellationException("Subscription is already active");
        }

        if (!Objects.equals(subscription.getUser().getId(), userService.getCurrentUser().getId())) {
            throw new UnauthorizedException("You are not authorized to activate this subscription");
        }

        // TODO: PAYMENT VALIDATION
        // TODO: VALIDATE LOGIC (UPDATE PLAN DETAILS OR NO !? <REAL WORLD>
        subscription.setIsActive(true);
        subscriptionRepository.save(subscription);

        return subscriptionMapper.toDTO(
                subscriptionRepository.save(subscription)
        );
    }

    @Override
    public SubscriptionResponse cancelSubscription(String subscriptionId, String reason) {
        Subscription subscription = findEntityById(subscriptionId);

        if (!subscription.getIsActive()) {
            throw new SubscriptionCancellationException("Subscription is already inactive");
        }

        if (!Objects.equals(subscription.getUser().getId(), userService.getCurrentUser().getId())) {
            throw new UnauthorizedException("You are not authorized to cancel this subscription");
        }

        subscription.setIsActive(false);
        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancelledReason(reason);


        return subscriptionMapper.toDTO(
                subscriptionRepository.save(subscription)
        );
    }

    // TODO: BACKGROUND JOB TO CANCEL EXPIRED SUBSCRIPTIONS (ex: INVOKE EVERY DAY)
    @Override
    public void deactivateExpiredSubscriptions() {
        List<Subscription> expiredSubscriptions = subscriptionRepository.findExpiredActiveSubscriptions();

        expiredSubscriptions.forEach(subscription -> {
            subscription.setIsActive(false);
            subscriptionRepository.save(subscription);
        });
    }


    // ====== HELPERS ======= //
    @Override
    public Subscription findEntityById(String id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Subscription not found with id " + id
                ));
    }
}
