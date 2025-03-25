package com.bandhan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FacilityProviderRequest {
    @NotBlank(message = "Provider name can not be blank")
    private String name;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number. Must be 10 digits.")
    private String contact;

    private Long facilityId;
}
