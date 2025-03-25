package com.bandhan.specification;

import org.springframework.data.jpa.domain.Specification;

import com.bandhan.entity.Booking;
import com.bandhan.entity.Event;
import com.bandhan.entity.User;
import com.bandhan.enums.BookingStatus;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class BookingSpecification {
    public static Specification<Booking> hasId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if(id == null)
                return criteriaBuilder.conjunction();
            
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    public static Specification<Booking> hasEventId(Long eventId) {
        return (root, query, criteriaBuilder) -> {
            if(eventId == null)
                return criteriaBuilder.conjunction();
            
            Join<Booking, Event> eventJoin = root.join("event", JoinType.INNER);
            return criteriaBuilder.equal(eventJoin.get("id"), eventId);
        };
    }

    public static Specification<Booking> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if(userId == null)
                return criteriaBuilder.conjunction();
            
            Join<Booking, User> userJoin = root.join("user", JoinType.INNER);
            return criteriaBuilder.equal(userJoin.get("id"), userId);
        };
    }

    public static Specification<Booking> hasStatus(BookingStatus bookingStatus) {
        return (root, query, criteriaBuilder) -> {
            if(bookingStatus == null)
                return criteriaBuilder.conjunction();
            
            return criteriaBuilder.equal(root.get("bookingStatus"), bookingStatus);
        };
    }
}
