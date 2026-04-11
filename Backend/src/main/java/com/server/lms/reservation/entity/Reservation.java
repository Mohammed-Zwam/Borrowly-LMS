package com.server.lms.reservation.entity;

import com.server.lms.book.entity.Book;
import com.server.lms.reservation.enums.ReservationStatus;
import com.server.lms.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus = ReservationStatus.PENDING;

    private LocalDateTime reservedAt;

    private LocalDateTime cancelledAt;

    private LocalDateTime availableAt;

    private LocalDateTime availableUntil;

    private LocalDateTime fulfilledAt;


    private long queuePosition;

    private Boolean notificationSent = false;

    @Column(length = 500)
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public boolean canBeCancelled() {
        return reservationStatus == ReservationStatus.PENDING || reservationStatus == ReservationStatus.AVAILABLE;
    }

    public boolean hasExpired() {
        return reservationStatus == ReservationStatus.AVAILABLE
                && availableUntil != null
                && LocalDateTime.now().isAfter(availableUntil);
    }

}
