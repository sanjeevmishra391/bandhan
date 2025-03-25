package com.bandhan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class EventRequest {
    @NotBlank(message = "Event name is required")
    private String name;

    @NotNull(message = "Event category ID is required")
    private Long eventCategoryId;
    
    @NotNull(message = "Event base price is required")
    @PositiveOrZero(message = "Base price must be greater than zero")
    private Double basePrice;

    private String description;
}
