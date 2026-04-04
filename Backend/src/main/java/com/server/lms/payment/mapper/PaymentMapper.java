package com.server.lms.payment.mapper;

import com.server.lms._shared.base.BaseMapper;
import com.server.lms.payment.dto.request.PaymentRequest;
import com.server.lms.payment.dto.response.PaymentResponse;
import com.server.lms.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class PaymentMapper extends BaseMapper<PaymentRequest, PaymentResponse, Payment> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "subscriptionId", source = "subscription.id")
    public abstract PaymentResponse toDTO(Payment payment);
}
