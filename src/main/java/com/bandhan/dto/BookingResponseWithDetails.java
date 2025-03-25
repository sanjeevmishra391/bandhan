package com.bandhan.dto;

import java.sql.Timestamp;
import java.util.List;

import com.bandhan.entity.Booking;
import com.bandhan.entity.BookingDate;
import com.bandhan.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingResponseWithDetails {
    private Long id;
    private Long userId;
    private Long eventId;
    private Double agreedPrice;
    private BookingStatus bookingStatus;
    private List<BookingDate> bookingDates;
    private Timestamp creationDate;
    private Timestamp lastUpdateDate;
    private EventResponseWithDetails event;
    private UserResponse user;

    public BookingResponseWithDetails(Booking booking, EventResponseWithDetails event, UserResponse user) {
        this.id = booking.getId();
        this.userId = booking.getUser().getId();
        this.eventId = booking.getEvent().getId();
        this.agreedPrice = booking.getAgreedPrice();
        this.bookingStatus = booking.getBookingStatus();
        this.bookingDates = booking.getBookingDates();
        this.creationDate = booking.getCreationDate();
        this.lastUpdateDate = booking.getLastUpdateDate();
        this.event = event;
        this.user = user;
    }
}
