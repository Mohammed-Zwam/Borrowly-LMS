package com.server.lms.subscription.mapper;

import com.server.lms._shared.base.BaseMapper;
import com.server.lms.subscription.dto.request.SubscriptionPlanRequest;
import com.server.lms.subscription.dto.response.SubscriptionPlanResponse;
import com.server.lms.subscription.entity.SubscriptionPlan;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class SubscriptionPlanMapper extends BaseMapper<SubscriptionPlanRequest, SubscriptionPlanResponse, SubscriptionPlan> {

}
