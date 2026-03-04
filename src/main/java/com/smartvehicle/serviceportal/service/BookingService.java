package com.smartvehicle.serviceportal.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.smartvehicle.serviceportal.entity.Booking;
import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.repository.BookingRepository;

@Service
public class BookingService {

	private static final int MAX_BOOKINGS_PER_SLOT = 3;

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private NotificationService notificationService;

	// ============================================================
	// CREATE BOOKING (CUSTOMER)
	// ============================================================
	public Booking createBooking(Booking booking) {

		boolean alreadyBooked = bookingRepo.existsByVehicleVehicleIdAndBookingDate(booking.getVehicle().getVehicleId(),
				booking.getBookingDate());

		if (alreadyBooked) {
			throw new RuntimeException("This vehicle already has a booking on the selected date");
		}

		int count = bookingRepo.countByBookingDateAndSlot(booking.getBookingDate(), booking.getSlot());

		if (count >= MAX_BOOKINGS_PER_SLOT) {
			throw new RuntimeException("Selected slot is full. Please choose another slot.");
		}

		booking.setStatus("BOOKED");
		Booking savedBooking = bookingRepo.save(booking);

		// 🔔 NOTIFICATION → CUSTOMER
		User customer = booking.getVehicle().getUser();
		notificationService.createNotification(customer,
				"Your booking has been confirmed for " + booking.getBookingDate());

		return savedBooking;
	}

	// ============================================================
	// UPDATE STATUS (SERVICE ADVISOR)
	// ============================================================
	public Booking updateStatus(Long bookingId, String status) {

		Booking booking = getBookingById(bookingId);
		booking.setStatus(status);

		Booking updatedBooking = bookingRepo.save(booking);

		User customer = booking.getVehicle().getUser();

		// 🔔 STATUS-BASED NOTIFICATIONS
		if ("IN_PROGRESS".equals(status)) {
			notificationService.createNotification(customer,
					"Service has started for your vehicle " + booking.getVehicle().getVehicleNumber());
		}

		if ("COMPLETED".equals(status)) {
			notificationService.createNotification(customer,
					"Service completed for your vehicle " + booking.getVehicle().getVehicleNumber());
		}

		return updatedBooking;
	}

	// ============================================================
	// CANCEL BOOKING (CUSTOMER)
	// ============================================================
	public Booking cancelBooking(Long bookingId) {

		Booking booking = getBookingById(bookingId);

		if ("CANCELLED".equals(booking.getStatus())) {
			throw new RuntimeException("Booking already cancelled");
		}

		if ("COMPLETED".equals(booking.getStatus())) {
			throw new RuntimeException("Completed booking cannot be cancelled");
		}

		if (booking.getBookingDate().isBefore(LocalDate.now())) {
			throw new RuntimeException("Past booking cannot be cancelled");
		}

		booking.setStatus("CANCELLED");
		Booking cancelledBooking = bookingRepo.save(booking);

		// 🔔 NOTIFICATION → CUSTOMER
		notificationService.createNotification(booking.getVehicle().getUser(),
				"Your booking on " + booking.getBookingDate() + " has been cancelled");

		return cancelledBooking;
	}

	// ============================================================
	// HISTORY & FETCH
	// ============================================================
	public List<Booking> getBookingHistoryByVehicle(Long vehicleId) {
		return bookingRepo.findByVehicleVehicleId(vehicleId);
	}

	public Booking getBookingById(Long bookingId) {
		return bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
	}

	public List<Booking> getBookingsForDate(LocalDate date) {
		return bookingRepo.findByBookingDate(date);
	}
	
	
	// ============================================================
	// Pagination
	// ============================================================
	public Page<Booking> getBookingsPaginated(String search, Pageable pageable) {

		if (search == null || search.trim().isEmpty()) {
			return bookingRepo.findAll(pageable);
		}

		return bookingRepo.findByVehicle_VehicleNumberContainingIgnoreCaseOrStatusContainingIgnoreCase(search, search,
				pageable);
	}
}
