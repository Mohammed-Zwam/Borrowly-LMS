package com.server.lms.payment.mapper;

import com.server.lms._shared.base.BaseMapper;
import com.server.lms.payment.dto.request.PaymentInitiateRequest;
import com.server.lms.payment.dto.request.PaymentRequest;
import com.server.lms.payment.dto.response.PaymentInitiateResponse;
import com.server.lms.payment.dto.response.PaymentResponse;
import com.server.lms.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class PaymentInitiateMapper extends BaseMapper<PaymentInitiateRequest, PaymentInitiateResponse, Payment> {

}
