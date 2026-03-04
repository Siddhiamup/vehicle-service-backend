package com.smartvehicle.serviceportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	// ============================================================
	// GET USER BY EMAIL (UTILITY / DEBUG / ADMIN USE)
	// ============================================================
	@GetMapping("/{email}")
	public ResponseEntity<?> getUserByEmail(@PathVariable String email) {

		User user = userService.findByEmail(email);

		if (user == null) {
			return ResponseEntity.badRequest().body("User not found");
		}

		return ResponseEntity.ok(user);
	}

	// ============================================================
	// ADMIN → GET ALL USERS
	// ============================================================
	@GetMapping
	public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {

		Pageable pageable = PageRequest.of(page, size);

		Page<User> usersPage = userService.getUsersPaginated(search, pageable);

		return ResponseEntity.ok(usersPage);
	}
}
