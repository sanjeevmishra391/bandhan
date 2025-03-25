package com.bandhan.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bandhan.dto.FacilityProviderRequest;
import com.bandhan.entity.FacilityProvider;
import com.bandhan.service.FacilityProviderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("facility_provider")
public class FacilityProviderController {
    @Autowired
    FacilityProviderService facilityProviderService;

    @PostMapping("/create")
    public  ResponseEntity<Map<String, Object>> addFacilityProvider(@Valid @RequestBody FacilityProviderRequest facilityProviderRequest) {
        return ResponseEntity.ok(facilityProviderService.createFacilityProvider(facilityProviderRequest));
    }
    
    @GetMapping("/cust/facility_providers")
    public ResponseEntity<Map<String, Object>> getAllFacilityProviders() {
        return ResponseEntity.ok(facilityProviderService.getAllFacilityProviders());
    }
    
    @GetMapping("/cust/search")
    public ResponseEntity<Map<String, Object>> getFacilityProviderByName(@RequestParam String name) {
        return ResponseEntity.ok(facilityProviderService.getFacilityProvidersByName(name));
    }

    @GetMapping("/by_facility/{id}")
    public ResponseEntity<Map<String, Object>> getFacilityProviderByFacilityId(@PathVariable Long id) {
        return ResponseEntity.ok(facilityProviderService.getFacilityProvidersByFacilityId(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateFacilityProviderById(@PathVariable Long id, @RequestBody FacilityProvider facilityProvider) {
        return ResponseEntity.ok(facilityProviderService.updateFacilityProviderById(id, facilityProvider));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFacilityProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityProviderService.deleteFacilityProviderById(id));
    }
}
