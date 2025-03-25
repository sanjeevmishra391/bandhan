package com.bandhan.dto;

import java.util.List;

import com.bandhan.enums.BookingStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @NotNull(message = "Event id cannot be null")
    private Long eventId;

    @NotNull(message = "User id cannot be null")
    private Long userId;

    @NotNull(message = "Agreed price cannot be null")
    @Positive(message = "Agreed price must be positive")
    private Double agreedPrice;

    @NotNull(message = "Time slot cannot be null")
    @Size(min = 1, message = "Time slot can not be empty")
    private List<TimeSlot> timeSlots;

    private BookingStatus bookingStatus = BookingStatus.CONFIRMED;
}
