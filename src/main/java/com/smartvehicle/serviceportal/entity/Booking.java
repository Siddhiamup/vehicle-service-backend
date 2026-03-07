package com.smartvehicle.serviceportal.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;

	// ====================================================
	// RELATIONS
	// ====================================================
	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;

	@ManyToOne
	@JoinColumn(name = "service_id")
	private ServiceMaster service;

	// ====================================================
	// BOOKING DETAILS
	// ====================================================
	private LocalDate bookingDate;
	private String slot;
	private String status;

	// ====================================================
	// 🔒 PRICE LOCK (NEW)
	// ====================================================
	private Double servicePriceAtBooking;
}
