package com.smartvehicle.serviceportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.entity.Vehicle;
import com.smartvehicle.serviceportal.service.VehicleService;
import com.smartvehicle.serviceportal.service.UserService;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private UserService userService;

	// ============================================================
	// CUSTOMER → ADD VEHICLE
	// ============================================================
	@PostMapping("/add")
	public ResponseEntity<?> addVehicle(@RequestBody Vehicle vehicle) {

		// ====================================================
		// Get logged-in user from JWT
		// ====================================================
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated()) {
			return ResponseEntity.status(401).body("Unauthorized");
		}

		String email = auth.getName();
		User user = userService.findByEmail(email);

		if (user == null) {
			return ResponseEntity.badRequest().body("User not found");
		}

		// ====================================================
		// Role validation
		// ====================================================
		if (!"CUSTOMER".equals(user.getRole())) {
			return ResponseEntity.status(403).body("Only customers can add vehicles");
		}

		return ResponseEntity.ok(vehicleService.addVehicle(vehicle, user));
	}

	// ============================================================
	// CUSTOMER → GET MY VEHICLES
	// ============================================================
	@GetMapping("/my")
	public ResponseEntity<?> getMyVehicles() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String email = auth.getName();
		User user = userService.findByEmail(email);

		return ResponseEntity.ok(vehicleService.getVehiclesByUser(user));
	}

//============================================================
//CUSTOMER → DELETE VEHICLE
//============================================================
	@DeleteMapping("/{vehicleId}")
	public ResponseEntity<?> deleteVehicle(@PathVariable Long vehicleId) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String email = auth.getName();
		User user = userService.findByEmail(email);

		vehicleService.deleteVehicle(vehicleId, user);

		return ResponseEntity.ok("Vehicle deleted successfully");
	}

	// ============================================================
	// CUSTOMER → UPDATE VEHICLE
	// ============================================================
	@PutMapping("/update/{vehicleId}")
	public ResponseEntity<?> updateVehicle(@PathVariable Long vehicleId, @RequestBody Vehicle vehicle) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String email = auth.getName();
		User user = userService.findByEmail(email);

		return ResponseEntity.ok(vehicleService.updateVehicle(vehicleId, vehicle, user));
	}

}
