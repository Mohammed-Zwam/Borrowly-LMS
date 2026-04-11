package com.server.lms.reservation.repository;

import com.server.lms._shared.base.BaseRepository;
import com.server.lms.reservation.entity.Reservation;
import com.server.lms.reservation.enums.ReservationStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends BaseRepository<Reservation, String> {

    @Query("""
                    SELECT r FROM Reservation r WHERE r.book.id = :bookId
                    AND r.reservationStatus = 'PENDING' ORDER BY r.reservedAt ASC
            """)
    List<Reservation> findPendingReservationsByBook(String bookId);

    @Query("""
            SELECT r FROM Reservation r WHERE r.book.id = :bookId
            AND r.reservationStatus = 'PENDING' ORDER BY r.reservedAt ASC LIMIT 1
            """)
    Optional<Reservation> findNextPendingReservation(String bookId);

    @Query("""
                SELECT COUNT(r) FROM Reservation r WHERE r.book.id = :bookId
                AND r.reservationStatus = 'PENDING'
            """)
    long countPendingReservationsByBook(String bookId);

    @Query("""
            SELECT COUNT(r) FROM Reservation r WHERE r.user.id = :userId
            AND (r.reservationStatus = 'PENDING' OR r.reservationStatus = 'AVAILABLE')
            """)
    long countActiveReservationsByUser(String userId);

    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM Reservation r
            WHERE r.user.id = :userId AND r.book.id = :bookId
            AND (r.reservationStatus = 'PENDING' OR r.reservationStatus = 'AVAILABLE')
            """)
    boolean hasActiveReservation(String userId, String bookId);


    @Query("""
            SELECT r FROM Reservation r
            WHERE r.reservationStatus = 'AVAILABLE'
            AND r.availableUntil < :currentDateTime
            """)
    List<Reservation> findExpiredReservations(LocalDateTime currentDateTime);


    Optional<Reservation> findActiveReservationByUserAndBook(String userId, String bookId);

    @Query("""
            SELECT r FROM Reservation r
            WHERE
                (:userId IS NULL OR r.user.id = :userId) AND
                (:bookId IS NULL OR r.book.id = :bookId) AND
                (:reservationStatus IS NULL OR r.reservationStatus = :reservationStatus) AND
                (:activeOnly = false OR (r.reservationStatus = 'PENDING' OR r.reservationStatus = 'AVAILABLE'))
            """)
    Page<Reservation> searchReservationsWithFilters(
            String userId,
            String bookId,
            ReservationStatus reservationStatus,
            boolean activeOnly,
            Pageable pageable);


}
