package com.bandhan.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bandhan.entity.Facility;
import com.bandhan.exception.BadRequestException;
import com.bandhan.repository.FacilityRepository;
import com.bandhan.utils.Util;

@Service
public class FacilityService {

    @Autowired
    FacilityRepository facilityRepository;

    public Map<String, Object> addFacility(Facility facility) {
        if(!facilityRepository.findByNameIgnoreCase(facility.getName()).isEmpty()) {
            throw new BadRequestException("Facility with the same name already exists");
        }

        Optional.ofNullable(facility.getName())
                .orElseThrow(() -> new BadRequestException("Name for facility cannot be null"));

        Facility newFacility = facilityRepository.save(facility);

        return Util.response("New facility added successfully", newFacility, "success");
    }

    public Map<String, Object> getAllFacilities() {
        List<Facility> facilities = facilityRepository.findAll();
        return Util.response("The list of facilities", facilities, "success");
    }

    public Map<String, Object> getFacilityByName(String name) {
        List<Facility> facilities = facilityRepository.findByNameContainingIgnoreCase(name);
        return Util.response("The list of facilities", facilities, "success");
    }

    public Map<String, Object> updateFacilityById(Long id, Facility facility) {
        Facility existingFacility = facilityRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No facility with the id " + id + " exists"));

        if(!facilityRepository.findByNameIgnoreCase(facility.getName()).isEmpty()) {
            throw new BadRequestException("Facility with the same name already exists");
        }

        Optional.ofNullable(facility.getName()).ifPresent(existingFacility::setName);
        Optional.ofNullable(facility.getDescription()).ifPresent(existingFacility::setDescription);

        Facility updatedFacility = facilityRepository.save(existingFacility);

        return Util.response("Facility updated successfully", updatedFacility, "success");
    }

    public Map<String, Object> deleteFacilityById(Long id) {
        facilityRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No facility with the id " + id + " exists"));

        facilityRepository.deleteById(id);

        return Util.response("Facility with id " + id + " deleted successfully", "success");
    }
}
