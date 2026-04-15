package com.server.lms.reservation.service;

import com.server.lms._shared.dto.PageResponse;
import com.server.lms._shared.exception.EntityNotFoundException;
import com.server.lms.book.entity.Book;
import com.server.lms.book.service.BookService;
import com.server.lms.loans.dto.request.BookLoanRequest;
import com.server.lms.loans.dto.request.BookLoanSearchRequest;
import com.server.lms.loans.enums.BookLoanState;
import com.server.lms.loans.service.BookLoanService;
import com.server.lms.reservation.dto.request.ReservationRequest;
import com.server.lms.reservation.dto.request.ReservationSearchRequest;
import com.server.lms.reservation.dto.response.ReservationResponse;
import com.server.lms.reservation.entity.Reservation;
import com.server.lms.reservation.enums.ReservationStatus;
import com.server.lms.reservation.exception.ReservationException;
import com.server.lms.reservation.mapper.ReservationMapper;
import com.server.lms.reservation.repository.ReservationRepository;
import com.server.lms.user.entity.User;
import com.server.lms.user.enums.UserRole;
import com.server.lms.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookLoanService bookLoanService;
    private final BookService bookService;
    private final UserService userService;
    private final ReservationMapper reservationMapper;


    @Override
    public ReservationResponse create(ReservationRequest reservationRequest) {
        return this.create(reservationRequest, userService.getCurrentUser());
    }

    public ReservationResponse create(ReservationRequest reservationRequest, String userId) {
        return this.create(reservationRequest, userService.findEntityById(userId));
    }

    private ReservationResponse create(ReservationRequest reservationRequest, User user) {
        boolean userHasBookLoan = bookLoanService.existsByUserIdAndBookIdAndStatus(user.getId(), reservationRequest.getBookId(), BookLoanState.BORROWED);
        if (userHasBookLoan) {
            throw new ReservationException("You already have loan on this book");
        }

        Book book = bookService.findEntityById(reservationRequest.getBookId()); // throw if book not found

        if (reservationRepository.hasActiveReservation(user.getId(), book.getId())) {
            throw new ReservationException("You already have an active reservation for this book");
        }

        if (book.getAvailableCopies() > 0) {
            throw new ReservationException("Book is already available !");
        }

        long activeReservationsForUser = reservationRepository.countActiveReservationsByUser(user.getId());

        int MAX_RESERVATIONS = 5;
        if (activeReservationsForUser >= MAX_RESERVATIONS) {
            throw new ReservationException("You have reached the maximum number of active reservations");
        }

        long pendingCount = reservationRepository.countPendingReservationsByBook(book.getId());

        Reservation reservation = Reservation.builder()
                .book(book)
                .user(user)
                .reservationStatus(ReservationStatus.PENDING)
                .reservedAt(LocalDateTime.now())
                .notificationSent(false)
                .queuePosition(pendingCount + 1)
                .notes(reservationRequest.getNotes())
                .build();
        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponse cancelReservation(String reservationId) {
        Reservation reservation = findEntityById(reservationId);
        User user = userService.getCurrentUser();

        // Cancellation by Owner or Admin
        if (!reservation.getUser().getId().equals(user.getId())
                && user.getRole() != UserRole.ADMIN
        ) {
            throw new ReservationException("You don't have permission to cancel this reservation");
        }

        if (!reservation.canBeCancelled()) {
            throw new ReservationException("This reservation cannot be cancelled");
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());

        reservation = reservationRepository.save(reservation);


        return reservationMapper.toDTO(reservation);
    }

    @Override
    @Transactional // ROLLBACK IF BORROWING FAILED
    public ReservationResponse completeReservation(String reservationId) {
        Reservation reservation = findEntityById(reservationId);

        if (reservation.getReservationStatus() != ReservationStatus.PENDING) {
            throw new ReservationException("This reservation cannot be completed, the book is already {" + reservation.getReservationStatus() + "}");
        }

        if (reservation.getBook().getAvailableCopies() <= 0) {
            throw new ReservationException("Book is not available for completion");
        }
        reservation.setReservationStatus(ReservationStatus.FULFILLED);
        reservation.setAvailableAt(LocalDateTime.now());
        reservation.setFulfilledAt(LocalDateTime.now());

        reservation = reservationRepository.save(reservation);

        BookLoanRequest bookLoanRequest = BookLoanRequest.builder()
                .bookId(reservation.getBook().getId())
                .borrowingDays(Integer.MAX_VALUE) // depend on the max borrowing days of the subscription plan
                .notes("Reservation completed")
                .build();

        bookLoanService.borrowBook(reservation.getUser().getId(), bookLoanRequest);

        return reservationMapper.toDTO(reservation);
    }

    @Override
    public PageResponse<ReservationResponse> findMyReservations(ReservationSearchRequest searchRequest) {
        User user = userService.getCurrentUser();
        searchRequest.setUserId(user.getId());
        return findAllReservations(searchRequest);
    }

    @Override
    public PageResponse<ReservationResponse> findAllReservations(ReservationSearchRequest searchRequest) {
        Page<Reservation> reservations = reservationRepository.searchReservationsWithFilters(
                searchRequest.getUserId(),
                searchRequest.getBookId(),
                searchRequest.getReservationStatus(),
                searchRequest.getIsActiveOnly() != null ? searchRequest.getIsActiveOnly() : false,
                searchRequest.generatePageable()
        );
        return PageResponse.<ReservationResponse>builder()
                .content(reservations.getContent().stream().map(reservationMapper::toDTO).toList())
                .build()
                .setPageInfo(reservations);

    }


    // ==== HELPERS ==== //
    @Override
    public Reservation findEntityById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id));
    }
}
