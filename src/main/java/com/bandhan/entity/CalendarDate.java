package com.bandhan.entity;

import java.time.LocalDate;

import com.bandhan.enums.CalendarDateStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "calendar_dates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarDate {
    @Id
    @Column(name = "calendar_date", unique = true, nullable = false)
    LocalDate calenderDate;

    @Enumerated(EnumType.STRING) 
    @Column(name = "status", nullable = false)
    CalendarDateStatus status;

    @Column
    private String description;
}
