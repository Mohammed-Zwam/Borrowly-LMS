package com.server.lms.subscription.service;

import com.server.lms._shared.dto.PageResponse;
import com.server.lms.subscription.dto.request.SubscriptionRequest;
import com.server.lms.subscription.dto.response.SubscriptionResponse;
import java.util.List;

public interface SubscriptionService {
    SubscriptionResponse getById(String id);

    List<SubscriptionResponse> getUserActiveSubscriptions();

    PageResponse<SubscriptionResponse> getAllSubscriptions();

    SubscriptionResponse subscribe /* Create */ (SubscriptionRequest dto);

    SubscriptionResponse activateSubscription(String subscriptionId, String paymentId);

    SubscriptionResponse cancelSubscription(String subscriptionId, String reason);

    void deactivateExpiredSubscriptions();
}
