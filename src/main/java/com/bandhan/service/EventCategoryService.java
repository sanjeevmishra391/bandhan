package com.bandhan.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bandhan.entity.EventCategory;
import com.bandhan.exception.BadRequestException;
import com.bandhan.exception.ResourceNotFoundException;
import com.bandhan.repository.EventCategoryRepository;
import com.bandhan.utils.Util;

@Service
public class EventCategoryService {
    @Autowired
    EventCategoryRepository eventCategoryRepository;

    public Map<String, Object> createEventCategory(EventCategory eventCategory) {
        // validations
        if(eventCategory.getName() == null || eventCategory.getName().isEmpty()) {
            throw new BadRequestException("Event category name can not be null");
        }
        
        if(eventCategoryRepository.existsByName(eventCategory.getName())) {
            throw new BadRequestException("Another event category with the name already exists");
        }

        EventCategory newEventCategory = eventCategoryRepository.save(eventCategory);

        return Util.response("New event category created successfully", newEventCategory, "success");
    }

    public Map<String, Object> getAllEventCategories() {
        List<EventCategory> eventCategories = eventCategoryRepository.findAll();
        return Util.response("List of all event categories", eventCategories, "success");
    }

    public Map<String, Object> getEventCategoriesByName(String name) {
        List<EventCategory> eventCategories = eventCategoryRepository.findByNameContainingIgnoreCase(name);
        return Util.response("List of all event categories with name containing { " + name + " }", eventCategories, "success");
    }

    public Map<String, Object> updateEventCategories(Long id, EventCategory eventCategory) {
        EventCategory existingEventCategory = eventCategoryRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Event category with id " + id + " not found"));

        Optional.ofNullable(eventCategory.getName()).ifPresent(existingEventCategory::setName);
        Optional.ofNullable(eventCategory.getSubCategory()).ifPresent(existingEventCategory::setSubCategory);
        Optional.ofNullable(eventCategory.getDescription()).ifPresent(existingEventCategory::setDescription);

        eventCategoryRepository.save(existingEventCategory);

        return Util.response("Event category with id " + id + " updated successfully", existingEventCategory, "success");
    }

    public Map<String, Object> deleteEventCategories(Long id) {
        EventCategory existingEventCategory = eventCategoryRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Event category with id " + id + " not found"));

        eventCategoryRepository.delete(existingEventCategory);

        return Util.response("Event category with id " + id + " deleted successfully", "success");
    }
}
