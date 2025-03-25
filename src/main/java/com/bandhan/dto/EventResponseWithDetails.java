package com.bandhan.dto;

import java.sql.Timestamp;

import com.bandhan.entity.Event;
import com.bandhan.entity.EventCategory;
import com.bandhan.enums.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventResponseWithDetails {
    private Long id;
    private String name;
    private String description;
    private Double basePrice;
    private EventStatus eventStatus;
    private Long eventCategoryId;
    private Timestamp creationDate;
    private Timestamp lastUpdateDate;
    private EventCategory eventCategory;

    public EventResponseWithDetails(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.basePrice = event.getBasePrice();
        this.eventStatus = event.getEventStatus();
        this.eventCategoryId = event.getEventCategory().getId();
        this.creationDate = event.getCreationDate();
        this.lastUpdateDate = event.getLastUpdateDate();
        this.eventCategory = event.getEventCategory();
    }
}
