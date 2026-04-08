package com.server.lms.penalty.service;

import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms._shared.exception.EntityNotFoundException;
import com.server.lms.loans.entity.BookLoan;
import com.server.lms.loans.service.BookLoanService;
import com.server.lms.payment.dto.request.PaymentInitiateRequest;
import com.server.lms.payment.dto.response.PaymentInitiateResponse;
import com.server.lms.payment.enums.PaymentType;
import com.server.lms.payment.service.PaymentService;
import com.server.lms.penalty.dto.request.PenaltyPaymentRequest;
import com.server.lms.penalty.dto.request.PenaltyRequest;
import com.server.lms.penalty.dto.request.PenaltyCancellationRequest;
import com.server.lms.penalty.dto.response.PenaltyResponse;
import com.server.lms.penalty.entity.Penalty;
import com.server.lms.penalty.enums.PenaltyState;
import com.server.lms.penalty.enums.PenaltyType;
import com.server.lms.penalty.exception.PenaltyException;
import com.server.lms.penalty.mapper.PenaltyMapper;
import com.server.lms.penalty.repository.PenaltyRepository;
import com.server.lms.penalty.specification.PenaltySpecification;
import com.server.lms.user.entity.User;
import com.server.lms.user.enums.UserRole;
import com.server.lms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PenaltyServiceImpl implements PenaltyService {

    private final BookLoanService bookLoanService;
    private final PenaltyRepository penaltyRepository;
    private final PenaltyMapper penaltyMapper;
    private final UserService userService;
    private final PaymentService paymentService;

    @Override
    public PenaltyResponse create(PenaltyRequest dto) {
        BookLoan bookLoan = bookLoanService.findEntityById(dto.getBookLoanId());

        if (!bookLoan.getUser().getId().equals(userService.getCurrentUser().getId())) {
            throw new PenaltyException("User is not the owner of the book loan");
        }

        Penalty penalty = Penalty.builder()
                .bookLoan(bookLoan)
                .user(bookLoan.getUser())
                .amount(dto.getAmount())
                .reason(dto.getReason())
                .notes(dto.getNotes())
                .penaltyType(dto.getPenaltyType())
                .penaltyState(PenaltyState.UNPAID)
                .build();

        penalty = penaltyRepository.save(penalty);

        return penaltyMapper.toDTO(penalty);
    }

    @Override
    public PaymentInitiateResponse payPenalty(String penaltyId, PenaltyPaymentRequest penaltyPaymentRequest) {
        Penalty penalty = findEntityById(penaltyId); // validate penalty

        if (penalty.getPenaltyState() == PenaltyState.PAID) {
            throw new PenaltyException("Penalty already paid");
        }

        if (penalty.getPenaltyState() == PenaltyState.CANCELLED) {
            throw new PenaltyException("Penalty already cancelled");
        }

        User user = userService.getCurrentUser();
        PaymentInitiateRequest paymentInitiateRequest = PaymentInitiateRequest.builder()
                .userId(user.getId())
                .penaltyId(penaltyId)
                .amount(penalty.getAmount())
                .paymentType(PaymentType.PENALTY)
                .paymentProvider(penaltyPaymentRequest.getPaymentProvider())
                .description("Payment for penalty: " + penalty.getReason())
                .build();

        return paymentService.initiatePayment(paymentInitiateRequest);
    }

    @Override
    public void markPenalityAsPaid(String penaltyId, long amount, String transactionId) {
        Penalty penalty = findEntityById(penaltyId);

        penalty.handlePenalityPayment(amount);
        penalty.setTransactionId(transactionId);

        penaltyRepository.save(penalty);
    }

    @Override
    public PenaltyResponse cancelPenalty(String penaltyId, String reason) {
        Penalty penalty = findEntityById(penaltyId);

        if (penalty.getPenaltyState() == PenaltyState.PAID) {
            throw new PenaltyException("Penalty already paid, cannot be cancelled");
        }

        if (penalty.getPenaltyState() == PenaltyState.CANCELLED) {
            throw new PenaltyException("Penalty already cancelled");
        }

        User ADMIN = userService.getCurrentUser();


        if (ADMIN.getRole() != UserRole.ADMIN) {
            throw new PenaltyException("Only admins can cancel penalties");
        }

        penalty.setPenaltyState(PenaltyState.CANCELLED);
        penalty.setCancelledBy(ADMIN);
        penalty.setCancellationReason(reason);
        penalty.setCancelledAt(LocalDateTime.now());
        penaltyRepository.save(penalty);

        return penaltyMapper.toDTO(penalty);
    }

    @Override
    public PageResponse<PenaltyResponse> findAllPenalties(PenaltyState penaltyState, PenaltyType penaltyType, PageRequestDTO pageRequest) {

        Page<Penalty> penaltyPage;

        Specification<Penalty> spec = Specification.where(
                PenaltySpecification.hasPenaltyState(penaltyState).and(PenaltySpecification.hasPenaltyType(penaltyType))
        );

        penaltyPage = penaltyRepository.findAll(spec, pageRequest.generatePageable());

        return PageResponse.<PenaltyResponse>builder()
                .content(penaltyPage.map(penaltyMapper::toDTO).getContent())
                .build()
                .setPageInfo(penaltyPage);
    }

    @Override
    public List<PenaltyResponse> findAllPenaltiesForUser(PenaltyState penaltyState, PenaltyType penaltyType) {
        User user = userService.getCurrentUser();

        Specification<Penalty> spec = Specification.where(
                PenaltySpecification.hasUserId(user.getId())
        );

        if (penaltyState != null) {
            spec = spec.and(PenaltySpecification.hasPenaltyState(penaltyState));
        }

        if (penaltyType != null) {
            spec = spec.and(PenaltySpecification.hasPenaltyType(penaltyType));
        }

        return penaltyRepository.findAll(spec).stream().map(penaltyMapper::toDTO).toList();
    }


    // ===== HELPERS ===== //
    @Override
    public Penalty findEntityById(String id) {
        return penaltyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Penalty not found with ID: " + id));
    }
}
