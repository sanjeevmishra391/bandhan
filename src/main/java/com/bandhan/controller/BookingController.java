package com.bandhan.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.bandhan.dto.BookingRequest;
import com.bandhan.service.BookingService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> addBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.createBooking(bookingRequest));
    }

    @GetMapping("/bookings")
    public ResponseEntity<Map<String, Object>> getAllBookings(@RequestParam(defaultValue = "false") Boolean includeEvent, @RequestParam(defaultValue = "false") Boolean includeUser) {
        return ResponseEntity.ok(bookingService.getAllBookings(includeEvent, includeUser));
    }
    
    @GetMapping("/cust/{userId}")
    public ResponseEntity<Map<String, Object>> getBookingByUserId(@PathVariable Long userId, @RequestParam(defaultValue = "false") Boolean includeEvent) {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId, includeEvent));
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBookings(
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) String bookingStatus,
        @RequestParam(required = false) Long eventId,
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String eventStartDateFrom,
        @RequestParam(required = false) String eventStartDateTo,
        @RequestParam(required = false, defaultValue = "false") Boolean includeEvent,
        @RequestParam(required = false, defaultValue = "false") Boolean includeUser
    ) {
        return ResponseEntity.ok(bookingService.searchBooking(id, bookingStatus, eventId, userId, eventStartDateFrom, eventStartDateTo, includeEvent, includeUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBookingById(@PathVariable Long id, @RequestBody BookingRequest bookingRequest, @RequestParam(defaultValue = "false") Boolean includeEvent, @RequestParam(defaultValue = "false") Boolean includeUser) {
        return ResponseEntity.ok(bookingService.updateBookingById(id, bookingRequest, includeEvent, includeUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.deleteBookingById(id));
    }
}
