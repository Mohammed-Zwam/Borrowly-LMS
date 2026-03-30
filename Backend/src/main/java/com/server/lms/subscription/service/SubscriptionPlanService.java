package com.server.lms.subscription.service;

import com.server.lms.subscription.dto.request.SubscriptionPlanRequest;
import com.server.lms.subscription.dto.response.SubscriptionPlanResponse;
import com.server.lms.subscription.entity.SubscriptionPlan;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlanResponse create(SubscriptionPlanRequest dto);

    SubscriptionPlanResponse update(String id, SubscriptionPlanRequest dto);

    void delete(String id);

    SubscriptionPlanResponse getById(String id);

    List<SubscriptionPlanResponse> getAll();

    SubscriptionPlan findEntityById(String id);

    void existsByPlanCode(String planCode);
}
