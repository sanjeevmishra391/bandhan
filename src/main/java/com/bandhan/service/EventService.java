package com.bandhan.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bandhan.dto.EventRequest;
import com.bandhan.entity.Event;
import com.bandhan.entity.EventCategory;
import com.bandhan.entity.User;
import com.bandhan.enums.EventStatus;
import com.bandhan.enums.UserRole;
import com.bandhan.exception.BadRequestException;
import com.bandhan.exception.ResourceNotFoundException;
import com.bandhan.repository.EventCategoryRepository;
import com.bandhan.repository.EventRepository;
import com.bandhan.utils.Util;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventCategoryRepository eventCategoryRepository;

    @Autowired
    UserService userService;

    public Map<String, Object> createEvent(EventRequest eventRequest) {
        // validations

        EventCategory category = eventCategoryRepository.findById(eventRequest.getEventCategoryId())
            .orElseThrow(() -> new BadRequestException("EventCategory not found"));

        Event event = Event.builder()
                            .name(eventRequest.getName())
                            .eventCategory(category)
                            .basePrice(eventRequest.getBasePrice())
                            .description(eventRequest.getDescription())
                            .build();

        Event newEvent = eventRepository.save(event);

        return Util.response("New event created successfully", newEvent, "success");
    }

    public Map<String, Object> getAllEvents() {
        User loggedUser = userService.getAuthenticatedUser();

        List<Event> events;
        // if the logged user is CUSTOMER then send only ACTIVE events;
        if(loggedUser.getUserRole() == UserRole.CUSTOMER)
            events = eventRepository.findByEventStatus(EventStatus.ACTIVE);
        else
            events = eventRepository.findAll();

        return Util.response("List of all events", events, "success");
    }

    public Map<String, Object> getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new BadRequestException("Invalid event id"));

        return Map.of("message", "Event with id " + id,
                "result", event);
    }

    public Map<String, Object> getEventsByStatus(String status) {
        EventStatus eventStatus = Arrays.stream(EventStatus.values()).filter(es -> es.toString().equals(status.toUpperCase())).findFirst()
                                                            .orElseThrow(() -> new BadRequestException("No such status exists"));

        List<Event> events = eventRepository.findByEventStatus(eventStatus);
        return Util.response("List of all events", events, "success");
    }

    public Map<String, Object> getEventsByName(String name) {
        List<Event> events = eventRepository.findByNameContainingIgnoreCase(name);
        return Util.response("List of all events with name containing { " + name + " }", events, "success");
    }

    public Map<String, Object> updateEvent(Long id, Event event) {
        Event existingEvent = eventRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Event with id " + id + " not found"));

        Optional.ofNullable(event.getName()).ifPresent(existingEvent::setName);
        Optional.ofNullable(event.getEventStatus()).ifPresent(existingEvent::setEventStatus);
        // Optional.ofNullable(event.getCategoryId()).ifPresent(existingEvent::setCategoryId);
        Optional.ofNullable(event.getDescription()).ifPresent(existingEvent::setDescription);
        Optional.ofNullable(event.getBasePrice()).ifPresent(price -> {
            if(price < 0) {
                throw new BadRequestException("Base price can not be negative");
            }
            existingEvent.setBasePrice(price);
        });

        eventRepository.save(existingEvent);

        return Util.response( "Event with id " + id + " updated successfully", existingEvent, "success");
    }

    public Map<String, Object> deleteEvent(Long id) {
        Event existingEvent = eventRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Event with id " + id + " not found"));

        eventRepository.delete(existingEvent);

        return Util.response("Event with id " + id + " deleted successfully", "success");
    }
}
