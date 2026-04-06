package com.server.lms.subscription.service;

import com.server.lms._shared.base.BaseService;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.payment.dto.response.PaymentInitiateResponse;
import com.server.lms.subscription.dto.request.SubscriptionRequest;
import com.server.lms.subscription.dto.response.SubscriptionResponse;
import com.server.lms.subscription.entity.Subscription;

import java.util.List;

public interface SubscriptionService extends BaseService<Subscription, String> {
    SubscriptionResponse getById(String id);

    SubscriptionResponse getUserActiveSubscription(String userId);

    PageResponse<SubscriptionResponse> getAllSubscriptions();

    PaymentInitiateResponse subscribe (SubscriptionRequest dto);

    SubscriptionResponse activateSubscription(String subscriptionId, String paymentId);

    SubscriptionResponse cancelSubscription(String subscriptionId, String reason);

    void deactivateExpiredSubscriptions();
}
