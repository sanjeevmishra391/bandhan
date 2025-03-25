package com.bandhan.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(
    name = "booking_dates"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDate {

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "bookingId", column = @Column(name = "booking_id")),
        @AttributeOverride(name = "calendarDate", column = @Column(name = "calendar_date")),
        @AttributeOverride(name = "startTime", column = @Column(name = "start_time")),
        @AttributeOverride(name = "endTime", column = @Column(name = "end_time"))
    })
    private BookingDateId id;

    @ManyToOne
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonBackReference
    private Booking booking;
}
