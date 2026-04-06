package com.server.lms.payment.service;

import com.server.lms._shared.base.BaseService;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.payment.dto.request.PaymentInitiateRequest;
import com.server.lms.payment.dto.request.PaymentRequest;
import com.server.lms.payment.dto.response.PaymentInitiateResponse;
import com.server.lms.payment.dto.response.PaymentResponse;
import com.server.lms.payment.entity.Payment;


public interface PaymentService extends BaseService<Payment, String> {
    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request);

    PaymentResponse verifyPayment(PaymentRequest request);

    PageResponse<PaymentResponse> getAllPayments(PageRequestDTO pageRequest);
}
