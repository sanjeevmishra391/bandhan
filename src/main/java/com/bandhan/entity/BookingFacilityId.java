package com.bandhan.entity;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookingFacilityId implements Serializable {
    private Long bookingId;
    private Long facilityId;
}
