package com.bandhan.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bandhan.dto.FacilityProviderRequest;
import com.bandhan.entity.Facility;
import com.bandhan.entity.FacilityProvider;
import com.bandhan.exception.BadRequestException;
import com.bandhan.exception.ResourceNotFoundException;
import com.bandhan.repository.FacilityProviderRepository;
import com.bandhan.repository.FacilityRepository;
import com.bandhan.utils.Util;

@Service
public class FacilityProviderService {
    
    @Autowired
    FacilityProviderRepository facilityProviderRepository;

    @Autowired
    FacilityRepository facilityRepository;

    public Map<String, Object> createFacilityProvider(FacilityProviderRequest facilityProviderRequest) {
        // validations
        Facility facility = facilityRepository.findById(facilityProviderRequest.getFacilityId())
            .orElseThrow(() -> new BadRequestException("Facility not found"));

        FacilityProvider facilityProvider = FacilityProvider.builder()
                                            .name(facilityProviderRequest.getName())
                                            .contact(facilityProviderRequest.getContact())
                                            .facility(facility)
                                            .build();

        FacilityProvider newFacilityProvider = facilityProviderRepository.save(facilityProvider);

        return Util.response("New facility provider created successfully", newFacilityProvider, "success");
    }

    public Map<String, Object> getAllFacilityProviders() {
        List<FacilityProvider> facilityProviders = facilityProviderRepository.findAll();
        return Util.response("List of all facility providers", facilityProviders, "success");
    }

    public Map<String, Object> getFacilityProvidersByName(String name) {
        List<FacilityProvider> facilityProviders = facilityProviderRepository.findByNameContainingIgnoreCase(name);
        return Util.response("List of all facility providers with name containing { " + name + " }", facilityProviders, "success");
    }

    public Map<String, Object> getFacilityProvidersByFacilityId(Long id) {
        // validations
        if(!facilityRepository.existsById(id)) {
            throw new BadRequestException("Facility id is not valid");
        }

        List<FacilityProvider> facilityProviders = facilityProviderRepository.findByFacilityId(id);

        return Util.response("List of all facility providers for facility id " + id, facilityProviders, "success");
    }
    

    public Map<String, Object> updateFacilityProviderById(Long id, FacilityProvider facilityProvider) {
        FacilityProvider existingFacilityProvider = facilityProviderRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Facility provider with id " + id + " not found"));

        Optional.ofNullable(facilityProvider.getName()).ifPresent(existingFacilityProvider::setName);
        Optional.ofNullable(facilityProvider.getContact()).ifPresent(existingFacilityProvider::setContact);

        facilityProviderRepository.save(existingFacilityProvider);

        return Util.response("Facility provider with id " + id + " updated successfully", existingFacilityProvider, "success");
    }

    public Map<String, Object> deleteFacilityProviderById(Long id) {
        FacilityProvider existingEventCategory = facilityProviderRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Facility provider with id " + id + " not found"));

        facilityProviderRepository.delete(existingEventCategory);

        return Util.response("Facility provider with id " + id + " deleted successfully", "success");
    }
}
