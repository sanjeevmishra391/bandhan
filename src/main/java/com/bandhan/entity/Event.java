package com.bandhan.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.bandhan.enums.EventStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private EventCategory eventCategory;

    @Builder.Default
    @Enumerated(EnumType.STRING) 
    @Column(name = "status", nullable = false)
    private EventStatus eventStatus = EventStatus.ACTIVE;

    @Column(name = "base_price", precision = 2)
    private Double basePrice;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "last_update_date")
    private Timestamp lastUpdateDate;
}
