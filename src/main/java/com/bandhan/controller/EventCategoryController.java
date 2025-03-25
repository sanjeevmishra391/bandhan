package com.bandhan.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bandhan.entity.EventCategory;
import com.bandhan.service.EventCategoryService;

@Controller
@RequestMapping("/event_category")
public class EventCategoryController {
    @Autowired
    EventCategoryService eventCategoryService;

    @PostMapping("/create")
    public  ResponseEntity<Map<String, Object>> addEventCategory(@RequestBody EventCategory eventCategory) {
        return ResponseEntity.ok(eventCategoryService.createEventCategory((eventCategory)));
    }
    
    @GetMapping("/cust/event_categories")
    public ResponseEntity<Map<String, Object>> getAllEventCategories() {
        return ResponseEntity.ok(eventCategoryService.getAllEventCategories());
    }
    
    @GetMapping("/cust/search")
    public ResponseEntity<Map<String, Object>> getEventCategoryByName(@RequestParam String name) {
        return ResponseEntity.ok(eventCategoryService.getEventCategoriesByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEventCategoryById(@PathVariable Long id, @RequestBody EventCategory eventCategory) {
        return ResponseEntity.ok(eventCategoryService.updateEventCategories(id, eventCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEventCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(eventCategoryService.deleteEventCategories(id));
    }
}
