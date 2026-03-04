package com.smartvehicle.serviceportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private Double amount;

    private String paymentStatus; // PENDING / PAID
}
