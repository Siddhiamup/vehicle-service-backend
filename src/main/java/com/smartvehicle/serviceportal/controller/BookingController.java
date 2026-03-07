package com.smartvehicle.serviceportal.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartvehicle.serviceportal.entity.Booking;
import com.smartvehicle.serviceportal.entity.ServiceMaster;
import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.entity.Vehicle;
import com.smartvehicle.serviceportal.service.BookingService;
import com.smartvehicle.serviceportal.service.ServiceMasterService;
import com.smartvehicle.serviceportal.service.UserService;
import com.smartvehicle.serviceportal.service.VehicleService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private ServiceMasterService serviceMasterService;

	@Autowired
	private UserService userService;

	// ============================================================
	// CUSTOMER → CREATE BOOKING
	// ============================================================
	@PostMapping("/create/{vehicleId}/{serviceId}")
	public ResponseEntity<?> createBooking(@PathVariable Long vehicleId, @PathVariable Long serviceId,
			@RequestBody Booking booking) {

		// Fetch vehicle
		Vehicle vehicle = vehicleService.getById(vehicleId);

		// Fetch service
		ServiceMaster service = serviceMasterService.getAllServices().stream()
				.filter(s -> s.getServiceId().equals(serviceId)).findFirst()
				.orElseThrow(() -> new RuntimeException("Service not found with ID: " + serviceId));

		// Attach relations
		booking.setVehicle(vehicle);
		booking.setService(service);

		return ResponseEntity.ok(bookingService.createBooking(booking));
	}

	// ============================================================
	// CUSTOMER → CANCEL BOOKING
	// ============================================================
	@PatchMapping("/cancel/{bookingId}")
	public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userService.findByEmail(email);
		Booking booking = bookingService.getBookingById(bookingId);

		// Ownership validation
		if (!booking.getVehicle().getUser().getUserId().equals(user.getUserId())) {

			return ResponseEntity.status(403).body("You cannot cancel someone else's booking");
		}

		return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
	}

	// ============================================================
	// SERVICE ADVISOR → UPDATE BOOKING STATUS
	// ============================================================
	@PatchMapping("/updateStatus/{bookingId}")
	public ResponseEntity<?> updateStatus(@PathVariable Long bookingId, @RequestParam String status) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User advisor = userService.findByEmail(email);

		// ✅ IMPORTANT:
		// role stored in DB = SERVICE_ADVISOR
		// authority used by Spring = ROLE_SERVICE_ADVISOR
		if (advisor == null || !"SERVICE_ADVISOR".equals(advisor.getRole())) {
			return ResponseEntity.status(403).body("Only service advisor can update booking status");
		}

		return ResponseEntity.ok(bookingService.updateStatus(bookingId, status));
	}

	// ============================================================
	// CUSTOMER → BOOKING HISTORY (BY VEHICLE)
	// ============================================================
	@GetMapping("/history/{vehicleId}")
	public ResponseEntity<?> bookingHistory(@PathVariable Long vehicleId) {

		return ResponseEntity.ok(bookingService.getBookingHistoryByVehicle(vehicleId));
	}

	// ============================================================
	// SERVICE ADVISOR → DAILY BOOKINGS DASHBOARD
	// ============================================================
	@GetMapping("/dashboard/{date}")
	public ResponseEntity<?> getDailyBookings(@PathVariable String date) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User advisor = userService.findByEmail(email);

		// Role check (NO ROLE_ PREFIX HERE)
		if (!"SERVICE_ADVISOR".equals(advisor.getRole())) {
			return ResponseEntity.status(403).body("Only service advisor can access dashboard");
		}

		return ResponseEntity.ok(bookingService.getBookingsForDate(LocalDate.parse(date)));
	}

	// ============================================================
	// Create AdminBookingController for Pagination
	// ============================================================
	@GetMapping("/admin/all")
	public ResponseEntity<?> getAllBookings(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String search) {

		Pageable pageable = PageRequest.of(page, size);

		Page<Booking> bookings = bookingService.getBookingsPaginated(search, pageable);

		return ResponseEntity.ok(bookings);
	}
}
