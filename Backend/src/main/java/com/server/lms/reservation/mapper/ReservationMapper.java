package com.server.lms.reservation.mapper;

import com.server.lms._shared.base.BaseMapper;
import com.server.lms.reservation.dto.request.ReservationRequest;
import com.server.lms.reservation.dto.response.ReservationResponse;
import com.server.lms.reservation.entity.Reservation;
import org.mapstruct.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class ReservationMapper extends BaseMapper<ReservationRequest, ReservationResponse, Reservation> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "bookIsbn", source = "book.isbn")
    @Mapping(target = "bookAuthor", source = "book.author")
    @Mapping(target = "isBookAvailable", expression = "java(reservation.getBook().getAvailableCopies() > 0)")
    public abstract ReservationResponse toDTO(Reservation reservation);

    @AfterMapping
    public void setComputedData(Reservation reservation, @MappingTarget ReservationResponse reservationResponse) {
        if (reservation.getAvailableUntil() != null) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(reservation.getAvailableUntil())) {
                long hours = Duration.between(now, reservation.getAvailableUntil()).toHours();
                reservationResponse.setHoursUntilExpiry(hours);
            } else {
                reservationResponse.setHoursUntilExpiry(0L);
            }
        }
        reservationResponse.setCanBeCancelled(reservation.canBeCancelled());
        reservationResponse.setExpired(reservation.hasExpired());
    }
}
