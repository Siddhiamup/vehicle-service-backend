package com.smartvehicle.serviceportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.entity.Vehicle;
import com.smartvehicle.serviceportal.repository.VehicleRepository;

@Service
public class VehicleService {

	@Autowired
	private VehicleRepository vehicleRepo;

	// ====================================================
	// ADD VEHICLE FOR LOGGED-IN CUSTOMER
	// ====================================================
	public Vehicle addVehicle(Vehicle vehicle, User user) {

		// Link vehicle to customer
		vehicle.setUser(user);

		return vehicleRepo.save(vehicle);
	}

	// ====================================================
	// GET VEHICLE BY ID (USED BY BOOKING)
	// ====================================================
	public Vehicle getById(Long vehicleId) {

		return vehicleRepo.findById(vehicleId)
				.orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));
	}

	// ============================================================
	// GET VEHICLES FOR LOGGED-IN CUSTOMER
	// ============================================================
	public List<Vehicle> getVehiclesByUser(User user) {

		return vehicleRepo.findByUser(user);
	}

	// ============================================================
	// DELETE VEHICLE
	// ============================================================
	public void deleteVehicle(Long vehicleId, User user) {

		Vehicle vehicle = vehicleRepo.findById(vehicleId).orElseThrow(() -> new RuntimeException("Vehicle not found"));

		// Ownership validation
		if (!vehicle.getUser().getUserId().equals(user.getUserId())) {
			throw new RuntimeException("Unauthorized to delete this vehicle");
		}

		vehicleRepo.delete(vehicle);
	}

	// ============================================================
	// UPDATE VEHICLE
	// ============================================================
	public Vehicle updateVehicle(Long vehicleId, Vehicle updatedVehicle, User user) {

		Vehicle vehicle = vehicleRepo.findById(vehicleId).orElseThrow(() -> new RuntimeException("Vehicle not found"));

		// Ownership validation
		if (!vehicle.getUser().getUserId().equals(user.getUserId())) {
			throw new RuntimeException("Unauthorized to update this vehicle");
		}

		vehicle.setVehicleNumber(updatedVehicle.getVehicleNumber());
		vehicle.setModel(updatedVehicle.getModel());
		vehicle.setFuelType(updatedVehicle.getFuelType());
		vehicle.setYear(updatedVehicle.getYear());

		return vehicleRepo.save(vehicle);
	}

}
