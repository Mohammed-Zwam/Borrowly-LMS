package com.server.lms.penalty.service;

import com.server.lms._shared.base.BaseService;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.payment.dto.response.PaymentInitiateResponse;
import com.server.lms.penalty.dto.request.PenaltyPaymentRequest;
import com.server.lms.penalty.dto.request.PenaltyRequest;
import com.server.lms.penalty.dto.request.PenaltyCancellationRequest;
import com.server.lms.penalty.dto.response.PenaltyResponse;
import com.server.lms.penalty.entity.Penalty;
import com.server.lms.penalty.enums.PenaltyState;
import com.server.lms.penalty.enums.PenaltyType;

import java.util.List;

public interface PenaltyService extends BaseService<Penalty, String> {
    PenaltyResponse create(PenaltyRequest dto);

    PaymentInitiateResponse payPenalty(String penaltyId, PenaltyPaymentRequest dto);

    void markPenalityAsPaid(String penaltyId, long amount, String transactionId);

    PenaltyResponse cancelPenalty(String penaltyId, String reason);

    PageResponse<PenaltyResponse> findAllPenalties(PenaltyState penaltyState, PenaltyType penaltyType, PageRequestDTO pageRequest);

    List<PenaltyResponse> findAllPenaltiesForUser(PenaltyState penaltyState, PenaltyType penaltyType);
}
