package com.smartvehicle.serviceportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.security.JwtUtil;
import com.smartvehicle.serviceportal.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	// ============================================================
	// REGISTER USER
	// ============================================================
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {

		// ❗ Password encoding is handled in service
		return ResponseEntity.ok(userService.saveUser(user));
	}

	// ============================================================
	// LOGIN → GENERATE JWT TOKEN
	// ============================================================
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String token = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(token);
	}
}
