package com.smartvehicle.serviceportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.service.NotificationService;
import com.smartvehicle.serviceportal.service.UserService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserService userService;

	// ============================================================
	// GET LOGGED-IN USER
	// ============================================================
	private User getLoggedInUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return userService.findByEmail(email);
	}

	// ============================================================
	// GET MY NOTIFICATIONS
	// ============================================================
	@GetMapping("/my")
	public ResponseEntity<?> getMyNotifications() {

		User user = getLoggedInUser();

		return ResponseEntity.ok(notificationService.getMyNotifications(user));
	}

	// ============================================================
	// MARK NOTIFICATION AS READ
	// ============================================================
	@PatchMapping("/read/{notificationId}")
	public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {

		notificationService.markAsRead(notificationId);

		return ResponseEntity.ok("Notification marked as read");
	}

	// ============================================================
	// GET UNREAD COUNT (FOR 🔔 BELL)
	// ============================================================
	@GetMapping("/unread-count")
	public ResponseEntity<?> getUnreadCount() {

		User user = getLoggedInUser();

		return ResponseEntity.ok(notificationService.getUnreadCount(user));
	}
}
