package com.smartvehicle.serviceportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.smartvehicle.serviceportal.entity.Booking;
import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.service.BookingService;
import com.smartvehicle.serviceportal.service.InvoiceService;
import com.smartvehicle.serviceportal.service.UserService;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserService userService;

	// ============================================================
	// SERVICE ADVISOR → GENERATE INVOICE
	// ============================================================
	@PostMapping("/generate/{bookingId}")
	public ResponseEntity<?> generateInvoice(@PathVariable Long bookingId) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User advisor = userService.findByEmail(email);

		if (advisor == null || !"SERVICE_ADVISOR".equals(advisor.getRole())) {

			return ResponseEntity.status(403).body("Only service advisor can generate invoice");
		}

		Booking booking = bookingService.getBookingById(bookingId);

		if (!"COMPLETED".equals(booking.getStatus())) {
			return ResponseEntity.badRequest().body("Invoice can be generated only after service completion");
		}

		return ResponseEntity.ok(invoiceService.generateInvoice(booking));
	}

	// ============================================================
	// CUSTOMER → VIEW SINGLE INVOICE
	// ============================================================
	@GetMapping("/view/{bookingId}")
	public ResponseEntity<?> viewInvoice(@PathVariable Long bookingId) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userService.findByEmail(email);

		Booking booking = bookingService.getBookingById(bookingId);

		if (!booking.getVehicle().getUser().getUserId().equals(user.getUserId())) {

			return ResponseEntity.status(403).body("Unauthorized access");
		}

		return ResponseEntity.ok(invoiceService.getInvoiceByBooking(booking));
	}

	// ============================================================
	// CUSTOMER → VIEW ALL OWN INVOICES (NEW)
	// ============================================================
	@GetMapping("/my")
	public ResponseEntity<?> getMyInvoices() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userService.findByEmail(email);

		return ResponseEntity.ok(invoiceService.getInvoicesForCustomer(user));
	}

	// ============================================================
	// ADMIN → MARK PAYMENT AS PAID
	// ============================================================
	@PatchMapping("/pay/{invoiceId}")
	public ResponseEntity<?> markPaid(@PathVariable Long invoiceId) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User admin = userService.findByEmail(email);

		if (admin == null || !"ADMIN".equals(admin.getRole())) {
			return ResponseEntity.status(403).body("Only admin can update payment status");
		}

		return ResponseEntity.ok(invoiceService.markAsPaid(invoiceId));
	}

	// ============================================================
	// CUSTOMER / ADMIN → DOWNLOAD INVOICE PDF
	// ============================================================
	@GetMapping("/download/{invoiceId}")
	public ResponseEntity<?> downloadInvoice(@PathVariable Long invoiceId) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userService.findByEmail(email);

		byte[] pdf = invoiceService.downloadInvoicePdf(invoiceId, user);

		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=invoice_" + invoiceId + ".pdf")
				.contentType(org.springframework.http.MediaType.APPLICATION_PDF).body(pdf);
	}

	// ============================================================
	// ADMIN → VIEW ALL INVOICES
	// ============================================================
	@GetMapping("/all")
	public ResponseEntity<?> getAllInvoices() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User admin = userService.findByEmail(email);

		return ResponseEntity.ok(invoiceService.getAllInvoices(admin));
	}

}
