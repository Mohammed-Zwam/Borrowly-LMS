package com.server.lms.subscription.service;

import com.server.lms._shared.base.BaseService;
import com.server.lms.subscription.dto.request.SubscriptionPlanRequest;
import com.server.lms.subscription.dto.response.SubscriptionPlanResponse;
import com.server.lms.subscription.entity.SubscriptionPlan;

import java.util.List;

public interface SubscriptionPlanService extends BaseService<SubscriptionPlan, String> {
    SubscriptionPlanResponse create(SubscriptionPlanRequest dto);

    SubscriptionPlanResponse update(String id, SubscriptionPlanRequest dto);

    void delete(String id);

    SubscriptionPlanResponse getById(String id);

    List<SubscriptionPlanResponse> getAll();

    void existsByPlanCode(String planCode);

    SubscriptionPlan getByPlanCode(String planCode);
}
