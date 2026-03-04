package com.smartvehicle.serviceportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smartvehicle.serviceportal.entity.Booking;
import com.smartvehicle.serviceportal.entity.Invoice;
import com.smartvehicle.serviceportal.entity.User;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	// ============================================================
	// EXISTING: Find invoice by booking
	// ============================================================
	Optional<Invoice> findByBooking(Booking booking);

	// ============================================================
	// CUSTOMER → Get all invoices of logged-in user
	// ============================================================
	List<Invoice> findByBookingVehicleUser(User user);

	// ============================================================
	// ADMIN → Get all invoices
	// (JpaRepository already provides findAll())
	// ============================================================

	// ============================================================
	// ADMIN → TOTAL REVENUE
	// ============================================================
	@Query("SELECT COALESCE(SUM(i.amount), 0) FROM Invoice i")
	Double getTotalRevenue();

	// ============================================================
	// ADMIN → PENDING PAYMENTS COUNT
	// ============================================================
	long countByPaymentStatus(String paymentStatus);

}
