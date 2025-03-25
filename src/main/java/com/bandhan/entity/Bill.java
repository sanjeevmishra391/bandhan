package com.bandhan.entity;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.bandhan.enums.BillPaymentType;
import com.bandhan.enums.BillStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "bills")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private Double amount;

    @Column
    private String description;

    @Column(name = "bill_date")
    private Date billDate;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_contact")
    private String receiverContact;

    @Enumerated(EnumType.STRING) 
    @Column(name = "payment_type", nullable = false)
    private BillPaymentType billPaymentType;

    @Enumerated(EnumType.STRING) 
    @Column(name = "status", nullable = false)
    private BillStatus billStatus;

    @Column(name = "reference_id")
    private String referenceId;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "last_update_date")
    private Timestamp lastUpdateDate;
}
