package com.bandhan.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.bandhan.entity.Facility;
import com.bandhan.service.FacilityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/facility")
public class FacilityController {
    @Autowired
    FacilityService facilityService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createFacility(@RequestBody Facility facility) {
        return ResponseEntity.ok(facilityService.addFacility(facility));
    }

    @GetMapping("/cust/facilities")
    public ResponseEntity<Map<String, Object>> getAllFacilities() {
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }
    
    @GetMapping("/cust/{name}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String name) {
        return ResponseEntity.ok(facilityService.getFacilityByName(name));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateFacilityById(@PathVariable Long id, @RequestBody Facility facility) {
        return ResponseEntity.ok(facilityService.updateFacilityById(id, facility));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFacilityById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.deleteFacilityById(id));
    }
}
