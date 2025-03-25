package com.bandhan.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingDateId implements Serializable {
    private Long bookingId;
    private LocalDate calendarDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDateId that = (BookingDateId) o;
        return Objects.equals(bookingId, that.bookingId) &&
               Objects.equals(calendarDate, that.calendarDate) &&
               Objects.equals(startTime, that.startTime) &&
               Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, calendarDate, startTime, endTime);
    }
}
