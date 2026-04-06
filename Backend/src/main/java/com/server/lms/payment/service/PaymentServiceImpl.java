package com.server.lms.payment.service;

import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.payment.dto.request.PaymentInitiateRequest;
import com.server.lms.payment.dto.request.PaymentRequest;
import com.server.lms.payment.dto.response.PaymentInitiateResponse;
import com.server.lms.payment.dto.response.PaymentLinkResponse;
import com.server.lms.payment.dto.response.PaymentResponse;
import com.server.lms.payment.entity.Payment;
import com.server.lms.payment.enums.PaymentStatus;
import com.server.lms.payment.event.publisher.PaymentEventPublisher;
import com.server.lms.payment.exception.PaymentFailureException;
import com.server.lms.payment.mapper.PaymentInitiateMapper;
import com.server.lms.payment.mapper.PaymentMapper;
import com.server.lms.payment.repository.PaymentRepository;
import com.server.lms.subscription.entity.Subscription;
import com.server.lms.subscription.repository.SubscriptionRepository;
import com.server.lms.user.entity.User;
import com.server.lms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final PaymentInitiateMapper paymentInitiateMapper;
    private final PaymentMapper paymentMapper;
    private final SubscriptionRepository subscriptionRepository;
    private final Map<String, PaymentGateway> paymentGateways; // Spring Boot injects all beans of type PaymentGateway
    private final PaymentEventPublisher paymentEventPublisher;


    @Override
    public PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) {
        User user = userService.getCurrentUser();

        Payment payment = paymentInitiateMapper.toEntity(request);
        payment.setTransactionId("TID_" + UUID.randomUUID());
        payment.setUser(user);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentProvider(request.getPaymentProvider());

        if (request.getSubscriptionId() != null) {
            Subscription subscription = subscriptionRepository.findById(request.getSubscriptionId())
                    .orElseThrow(() -> new RuntimeException("Subscription not found with id " + request.getSubscriptionId()));
            payment.setSubscription(subscription);
        }

        PaymentGateway gateway = paymentGatewayFactory(request.getPaymentProvider().name());

        PaymentLinkResponse paymentLinkResponse = gateway.createPaymentLink(user, payment);
        payment.setGatewayPaymentOrderId(paymentLinkResponse.getId());
        payment.setPaymentStatus(PaymentStatus.PROCESSING);
        payment.setInitiatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        return PaymentInitiateResponse.builder()
                .paymentProvider(request.getPaymentProvider())
                .id(payment.getId())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .description(payment.getDescription())
                .gatewayPaymentOrderId(paymentLinkResponse.getId())
                .checkoutUrl(paymentLinkResponse.getUrl())
                .subscriptionId(request.getSubscriptionId())
                .build();
    }

    @Override
    public PaymentResponse verifyPayment(PaymentRequest request) {
        PaymentGateway gateway = paymentGatewayFactory(request.getPaymentProvider().name());

        Map<String, String> paymentValidator = gateway.validatePayment(request.getGatewayPaymentOrderId());

        Payment payment = findEntityById(paymentValidator.get("payment_id"));

        if (!paymentValidator.get("isValidPayment").equals("true")) {
            payment.setPaymentStatus(PaymentStatus.FAILED);

            throw new PaymentFailureException("Payment verification failed for order: " + request.getGatewayPaymentOrderId());
        }
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCompletedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        // TODO: Activate Subscription & EVENTS (NOTIFICATIONS)
        paymentEventPublisher.publishPaymentSuccessEvent(payment);



        return paymentMapper.toDTO(payment);
    }

    @Override
    public PageResponse<PaymentResponse> getAllPayments(PageRequestDTO pageRequest) {
        Page<Payment> paymentsPage = paymentRepository.findAll(pageRequest.generatePageable());

        return PageResponse.<PaymentResponse>builder()
                .content(
                        paymentsPage.stream()
                                .map(paymentMapper::toDTO)
                                .collect(Collectors.toList())
                )
                .build()
                .setPageInfo(paymentsPage);
    }

    // ===== HELPERS ===== //

    private PaymentGateway paymentGatewayFactory(String paymentProvider) {
        PaymentGateway gateway = paymentGateways.get(paymentProvider);
        if (gateway == null) {
            throw new PaymentFailureException("Unsupported payment provider: " + paymentProvider);
        }
        return gateway;
    }


    @Override
    public Payment findEntityById(String s) {
        return paymentRepository.findById(s).orElseThrow(() -> new RuntimeException("Payment not found with id " + s));
    }
}
