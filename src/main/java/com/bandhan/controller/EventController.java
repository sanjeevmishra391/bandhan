package com.bandhan.controller;

import com.bandhan.dto.EventRequest;
import com.bandhan.entity.Event;
import com.bandhan.service.EventService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    EventService eventService;

    @PostMapping("/create")
    public  ResponseEntity<Map<String, Object>> addEvent(@Valid @RequestBody EventRequest eventRequest) {
        return ResponseEntity.ok(eventService.createEvent(eventRequest));
    }
    
    @GetMapping("/cust/events")
    public ResponseEntity<Map<String, Object>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
    
    @GetMapping("/cust/search")
    public ResponseEntity<Map<String, Object>> getEventByName(@RequestParam String name) {
        return ResponseEntity.ok(eventService.getEventsByName(name));
    }

    @GetMapping("/by_status/{status}")
    public ResponseEntity<Map<String, Object>> getEventByStatus(@PathVariable String status) {
        return ResponseEntity.ok(eventService.getEventsByStatus(status));
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEventById(@PathVariable Long id, @RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.deleteEvent(id));
    }
}
