package com.bandhan.service;

import com.bandhan.dto.*;
import com.bandhan.entity.*;
import com.bandhan.enums.*;
import com.bandhan.repository.*;
import com.bandhan.exception.BadRequestException;
import com.bandhan.exception.ResourceNotFoundException;
import com.bandhan.specification.BookingSpecification;
import com.bandhan.utils.Util;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CalendarDateRepository calendarDateRepository;

    @Autowired
    private BookingDateRepository bookingDateRepository;

    /**
     * Create a new booking after validations.
     * Booking Dates cases
     * 1. Booking occurs in a single date
     * 2. Boking occurs in mutiple dates
     * 2.1. Continuous timing
     * 2.2. Fragmented timing
     */
    @Transactional
    public Map<String, Object> createBooking(BookingRequest bookingRequest) {
        User user = (User) userService.getUserById(bookingRequest.getUserId()).get("result");
        Event event = validateEventExists(bookingRequest.getEventId());
        // there should be no overlapping in request time slots :: algorithm
        // for each time slot start should be before time slot end :: isAfter
        // there should be no overlapping in already booked timeslots. :: query
        validateTimeSlots(bookingRequest.getTimeSlots());

        Booking newBooking = Booking.builder()
                                .user(user)
                                .event(event)
                                .agreedPrice(bookingRequest.getAgreedPrice())
                                .bookingStatus(BookingStatus.CONFIRMED)
                                .build();

        // Collect BookingDates and distinct CalendarDates
        Set<LocalDate> distinctDates = new HashSet<>();
        List<BookingDate> bookingDates = bookingRequest.getTimeSlots().stream()
            .map(slot -> {
                distinctDates.add(slot.getDate());
                return BookingDate.builder()
                    .id(new BookingDateId(null, slot.getDate(), slot.getStartTime(), slot.getEndTime()))
                    .booking(newBooking)
                    .build();
            })
            .collect(Collectors.toList());

        bookingRepository.save(newBooking);
        bookingDates.forEach(bd -> bd.getId().setBookingId(newBooking.getId()));
        newBooking.setBookingDates(bookingDates);
        bookingDateRepository.saveAll(bookingDates);
        bookingRepository.save(newBooking);

        // Collect and persist CalendarDates
        List<CalendarDate> calendarDates = distinctDates.stream()
            .map(date -> {
                // Fetch existing booking times for the date
                List<BookingDate> existingBookings = bookingDateRepository.findById_CalendarDate(date);

                // Combine existing time slots with new ones
                List<TimeSlot> allTimeSlots = existingBookings.stream()
                        .map(bd -> new TimeSlot(bd.getId().getCalendarDate(), bd.getId().getStartTime(), bd.getId().getEndTime()))
                        .collect(Collectors.toList());
                
                // Check if the date is fully booked
                boolean isFullyBooked = isDateFullyBooked(allTimeSlots);

                return CalendarDate.builder()
                        .calenderDate(date)
                        .status(isFullyBooked ? CalendarDateStatus.FULLY_BOOKED : CalendarDateStatus.PARTIALLY_BOOKED)
                        .build();
            })
            .collect(Collectors.toList());

        calendarDateRepository.saveAll(calendarDates);

        BookingResponseWithDetails bookingWithDetails = new BookingResponseWithDetails(newBooking, null, null);

        bookingWithDetails = addEventWithBooking(bookingWithDetails, true);
        bookingWithDetails = addUserWithBooking(bookingWithDetails, true);

        return Util.response("Booking created successfully", bookingWithDetails, "success");
    }

    /**
     * Fetches all the bookings
     */
    public Map<String, Object> getAllBookings(boolean includeEvent, boolean includeUser) {
        List<Booking> bookings =  bookingRepository.findAll();
        List<BookingResponseWithDetails> bookingsWithDetails = bookings.stream().map(booking -> 
            new BookingResponseWithDetails(booking, null, null)).collect(Collectors.toList());

        bookingsWithDetails = addEventWithBooking(bookingsWithDetails, includeEvent);
        bookingsWithDetails = addUserWithBooking(bookingsWithDetails, includeUser);
        return Util.response("All bookings", bookingsWithDetails, "success");
    }

    /**
     * Get bookings by user ID.
     */
    public Map<String, Object> getBookingsByUserId(Long userId, boolean includeEvent) {
        User reqUser = (User) userService.getUserById(userId).get("result");

        User loggedUser = userService.getAuthenticatedUser();

        if(loggedUser.getUserRole() == UserRole.CUSTOMER && reqUser.getId() != loggedUser.getId()) {
            throw new BadRequestException("You do not have access to this data");
        }

        List<Booking> bookings = bookingRepository.findByUserId(userId);
        List<BookingResponseWithDetails> bookingsWithDetails = bookings.stream().map(booking -> 
            new BookingResponseWithDetails(booking, null, null)).collect(Collectors.toList());
        
        bookingsWithDetails = addEventWithBooking(bookingsWithDetails, includeEvent);

        return Util.response("Bookings of user ID " + userId, bookingsWithDetails, "success");
    }

    /**
     * Search for bookings based on criteria.
     */
    public Map<String, Object> searchBooking(Long id, String bookingStatusStr, Long eventId, Long userId, String eventStartDateFrom, String eventStartDateTo, boolean includeEvent, boolean includeUser) {
        BookingStatus bookingStatus = null;
        if (bookingStatusStr != null) {
            bookingStatus = validateBookingStatus(bookingStatusStr);
        }

        Specification<Booking> spec = Specification
                .where(BookingSpecification.hasStatus(bookingStatus))
                .and(BookingSpecification.hasId(id))
                .and(BookingSpecification.hasEventId(eventId))
                .and(BookingSpecification.hasUserId(userId));

        List<BookingResponseWithDetails> bookingsWithDetails = bookingRepository.findAll(spec).stream().map(booking -> 
                new BookingResponseWithDetails(booking, null, null)).collect(Collectors.toList());
        
        bookingsWithDetails = addEventWithBooking(bookingsWithDetails, includeEvent);
        bookingsWithDetails = addUserWithBooking(bookingsWithDetails, includeUser);

        return Util.response("Search result", bookingsWithDetails, "success");
    }

    /**
     * Update a booking by ID.
     */
    @Transactional
    public Map<String, Object> updateBookingById(Long id, BookingRequest bookingRequest, Boolean includeEvent, Boolean includeUser) {
        // Fetch the existing booking
        Booking existingBooking = getExistingBooking(id);

        // Validate and update booking status
        if (bookingRequest.getBookingStatus() != null) {
            validateBookingStatus(bookingRequest.getBookingStatus().toString());
            existingBooking.setBookingStatus(bookingRequest.getBookingStatus());
        }

        // Validate and update event if provided
        if (bookingRequest.getEventId() != null) {
            Event event = validateEventExists(bookingRequest.getEventId());
            existingBooking.setEvent(event);
        }

        // Validate and update user if provided
        if (bookingRequest.getUserId() != null) {
            User user = (User) userService.getUserById(bookingRequest.getUserId()).get("result");
            existingBooking.setUser(user);
        }

        // Validate and update agreed price
        if(bookingRequest.getAgreedPrice() != null) {
            if(bookingRequest.getAgreedPrice() <= 0) {
                throw new BadRequestException("Agreed price can not negative");
            }
            existingBooking.setAgreedPrice(bookingRequest.getAgreedPrice());
        }

        // Validate and update time slots if provided
        if (bookingRequest.getTimeSlots() != null && !bookingRequest.getTimeSlots().isEmpty()) {
            validateTimeSlots(bookingRequest.getTimeSlots());

            // Get all the dates in booking_dates for current booking
            HashSet<LocalDate> distinctPrevBookingDates = new HashSet<>();

            for(BookingDate date : existingBooking.getBookingDates()) {
                distinctPrevBookingDates.add(date.getId().getCalendarDate());
            }

            // Remove from calendar date only if no other booking for current date
            for(LocalDate date : distinctPrevBookingDates) {
                List<BookingDate> otherBookingDates = bookingDateRepository.findByCalendarDateAndNotBookingId(date, id);
                if(otherBookingDates.isEmpty()) {
                    calendarDateRepository.deleteById(date);
                }
            }

            // Remove existing BookingDate records for this booking
            bookingDateRepository.deleteAllByBookingId(existingBooking.getId());
            bookingDateRepository.flush(); 

            // Recreate new BookingDate entries
            Set<LocalDate> distinctDates = new HashSet<>();
            List<BookingDate> newBookingDates = new ArrayList<>();

            for (TimeSlot timeSlot : bookingRequest.getTimeSlots()) {
                BookingDateId bookingDateId = new BookingDateId(existingBooking.getId(), timeSlot.getDate(), timeSlot.getStartTime(), timeSlot.getEndTime());
                BookingDate bookingDate = BookingDate.builder()
                        .id(bookingDateId)
                        .booking(existingBooking)
                        .build();
                newBookingDates.add(bookingDate);
                distinctDates.add(timeSlot.getDate());
            }

            bookingDateRepository.saveAll(newBookingDates);

            // Update CalendarDate status
            List<CalendarDate> calendarDates = distinctDates.stream()
                .map(date -> {
                    boolean isFullyBooked = checkIfFullyBooked(date);
                    return CalendarDate.builder()
                            .calenderDate(date)
                            .status(isFullyBooked ? CalendarDateStatus.FULLY_BOOKED : CalendarDateStatus.PARTIALLY_BOOKED)
                            .build();
                })
                .collect(Collectors.toList());

            calendarDateRepository.saveAll(calendarDates);
        }

        // Save updated booking
        bookingRepository.save(existingBooking);

        BookingResponseWithDetails bookingResponseWithDetails = new BookingResponseWithDetails(existingBooking, null, null);
        bookingResponseWithDetails = addEventWithBooking(bookingResponseWithDetails, includeEvent);
        bookingResponseWithDetails = addUserWithBooking(bookingResponseWithDetails, includeUser);

        return Util.response("Booking updated successfully", bookingResponseWithDetails, "success");
    }

    /**
     * Delete a booking by ID.
     */
    public Map<String, Object> deleteBookingById(Long id) {
        Booking booking = getExistingBooking(id);
        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("Only confirmed bookings can be deleted.");
        }
        bookingRepository.deleteById(id);
        return Util.response("Booking deleted successfully", "success");
    }

    /* =================== PRIVATE HELPER METHODS =================== */
    
    /**
     * Validate if an event exists.
     */
    private Event validateEventExists(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new BadRequestException("Invalid event ID."));
    }

    /**
     * Validate time slots
     */
    private List<TimeSlot> validateTimeSlots(List<TimeSlot> timeSlots) {
        if (timeSlots == null || timeSlots.isEmpty()) {
            throw new BadRequestException("Booking time slots cannot be empty.");
        }
    
        // Sort time slots by start time
        Collections.sort(timeSlots, (a, b) -> {
            if(a.getDate().equals(b.getDate()))
                return a.getStartTime().compareTo(b.getStartTime());
            
            return a.getDate().compareTo(b.getDate());
        });
    
        for (int i = 0; i < timeSlots.size(); i++) {
            TimeSlot curr = timeSlots.get(i);
    
            // Check if start is after end (invalid time slot)
            if (curr.getStartTime().isAfter(curr.getEndTime())) {
                throw new BadRequestException("Booking start time cannot be after end time.");
            }

            List<BookingDate> overlappingBookingDates = bookingDateRepository.findOverlappingBookings(curr.getDate(), curr.getStartTime(), curr.getEndTime());
            System.out.println(overlappingBookingDates.size());
            if(!overlappingBookingDates.isEmpty()) {
                throw new BadRequestException("Booking with the requested time slot is already present");
            }

            // skip the below check for the first time slot
            if(i == 0)
                continue;
    
            // Check for overlapping time slots
            TimeSlot prev = timeSlots.get(i-1);
            if (prev.getDate().equals(curr.getDate()) && prev.getEndTime().isAfter(curr.getStartTime())) {
                throw new BadRequestException("Booking time slots are overlapping.");
            }
        }

        return timeSlots;
    }

    /**
     * Check if date is fully booked
     */
    private boolean isDateFullyBooked(List<TimeSlot> timeSlots) {
        // Sort time slots by start time
        timeSlots.sort(Comparator.comparing(TimeSlot::getStartTime));

        LocalTime start = LocalTime.MIN; // 00:00:00
        LocalTime end = LocalTime.MAX;   // 23:59:59

        // Merge overlapping time slots and check if the entire day is covered
        LocalTime currentEnd = start;
        for (TimeSlot slot : timeSlots) {
            if (slot.getStartTime().isAfter(currentEnd)) {
                return false; // There is a gap, so it's not fully booked
            }
            currentEnd = slot.getEndTime().isAfter(currentEnd) ? slot.getEndTime() : currentEnd;
        }

        
        currentEnd = currentEnd.plusNanos(999999999);
        return currentEnd.equals(end); // Fully booked only if last slot covers till 23:59
    }

    private boolean checkIfFullyBooked(LocalDate date) {
        List<BookingDate> existingBookingSlots = bookingDateRepository.findById_CalendarDate(date);
        LocalTime currentEnd = existingBookingSlots.get(0).getId().getEndTime();

        if(currentEnd != LocalTime.MIN) {
            return false;
        }

        for(BookingDate bookingDate : existingBookingSlots) {
            if(bookingDate.getId().getStartTime().isAfter(currentEnd)) {
                return false;
            }

            currentEnd = bookingDate.getId().getEndTime().isAfter(currentEnd) ? bookingDate.getId().getEndTime() : currentEnd;
        }

        currentEnd = currentEnd.plusNanos(999999999);

        return currentEnd.equals(LocalTime.MAX);
    }

    /**
     * Validate booking status.
     */
    private BookingStatus validateBookingStatus(String status) {
        return Arrays.stream(BookingStatus.values()).filter(es -> es.toString().equals(status.toUpperCase())).findFirst()
                                                            .orElseThrow(() -> new BadRequestException("Invalid booking status"));
    }

    /**
     * Fetch an existing booking or throw an exception.
     */
    private Booking getExistingBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + id + " not found"));
    }

    /**
     * Utility method to return event with booking details
     */
    private BookingResponseWithDetails addEventWithBooking(BookingResponseWithDetails booking, boolean includeEvent) {
        Event event = includeEvent ? eventRepository.findById(booking.getEventId()).orElse(null) : null;
        EventResponseWithDetails eventResponseWithDetails = includeEvent ? new EventResponseWithDetails(event) : null;
        return new BookingResponseWithDetails(
                    booking.getId(),
                    booking.getUserId(),
                    booking.getEventId(),
                    booking.getAgreedPrice(),
                    booking.getBookingStatus(),
                    booking.getBookingDates(),
                    booking.getCreationDate(),
                    booking.getLastUpdateDate(),
                    eventResponseWithDetails,
                    booking.getUser());
    }

    /**
     * Utility method to return event with booking details
     */
    private List<BookingResponseWithDetails> addEventWithBooking(List<BookingResponseWithDetails> bookings, boolean includeEvent) {
        return bookings.stream().map(booking -> {
            return addEventWithBooking(booking, includeEvent);
        }).collect(Collectors.toList());
    }

    private BookingResponseWithDetails addUserWithBooking(BookingResponseWithDetails booking, boolean includeUser) {
        User user = includeUser ? userRepository.findById(booking.getUserId()).orElse(null) : null;
        UserResponse userResponse = includeUser ? new UserResponse(user) : null;
        return new BookingResponseWithDetails(
                    booking.getId(), 
                    booking.getUserId(), 
                    booking.getEventId(), 
                    booking.getAgreedPrice(), 
                    booking.getBookingStatus(),
                    booking.getBookingDates(),
                    booking.getCreationDate(),
                    booking.getLastUpdateDate(),
                    booking.getEvent(), 
                    userResponse);
    }

    private List<BookingResponseWithDetails> addUserWithBooking(List<BookingResponseWithDetails> bookings, boolean includeUser) {
        return bookings.stream().map(booking -> {
            return addUserWithBooking(booking, includeUser);
        }).collect(Collectors.toList());
    }
}
