package com.bandhan.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "booking_facilities")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingFacility {
    @EmbeddedId
    private BookingFacilityId id; // Composite Primary Key

    @ManyToOne
    @MapsId("bookingId") // Links bookingId in BookingFacilityPK to Booking entity
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @MapsId("facilityId") // Links facilityId in BookingFacilityPK to Facility entity
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;


    @Column(name = "facility_cost", nullable = false)
    private Double facilityCost;

    @Column(name = "facility_description")
    private String facilityDescription;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "last_update_date")
    private Timestamp lastUpdateDate;
}
