package com.server.lms.subscription.repository;

import com.server.lms._shared.base.BaseRepository;
import com.server.lms.subscription.entity.SubscriptionPlan;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends BaseRepository<SubscriptionPlan, String> {
    Boolean existsByPlanCode(String planCode);
    Optional<SubscriptionPlan> findByPlanCode(String planCode);
}
