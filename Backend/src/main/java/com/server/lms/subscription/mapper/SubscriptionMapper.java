package com.server.lms.subscription.mapper;

import com.server.lms._shared.base.BaseMapper;
import com.server.lms.subscription.dto.request.SubscriptionRequest;
import com.server.lms.subscription.dto.response.SubscriptionResponse;
import com.server.lms.subscription.entity.Subscription;
import com.server.lms.subscription.service.SubscriptionPlanService;
import com.server.lms.user.mapper.UserMapper;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SubscriptionPlanMapper.class, UserMapper.class}
)
public abstract class SubscriptionMapper extends BaseMapper<SubscriptionRequest, SubscriptionResponse, Subscription> {

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;


    @BeforeMapping
    public void getEntitiesFromIds(SubscriptionRequest dto, @MappingTarget Subscription entity) {
        entity.setSubscriptionPlan(subscriptionPlanService.findEntityById(dto.getSubscriptionPlanId()));
    }
}
