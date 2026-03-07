package com.smartvehicle.serviceportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartvehicle.serviceportal.entity.ServiceMaster;
import com.smartvehicle.serviceportal.repository.BookingRepository;
import com.smartvehicle.serviceportal.repository.ServiceRepository;

@Service
public class ServiceMasterService {

	@Autowired
	private ServiceRepository serviceRepo;

	@Autowired
	private BookingRepository bookingRepo;

	// ============================================================
	// ADD SERVICE
	// ============================================================

	public ServiceMaster addService(ServiceMaster service) {
		return serviceRepo.save(service);
	}

	// ============================================================
	// GET ALL SERVICES
	// ============================================================

	public List<ServiceMaster> getAllServices() {
		return serviceRepo.findAll();
	}

	// ============================================================
	// DELETE SERVICE (SAFE DELETE)
	// ============================================================

	public void deleteService(Long serviceId) {

		// ✅ Check service exists
		ServiceMaster service = serviceRepo.findById(serviceId)
				.orElseThrow(() -> new RuntimeException("Service not found"));

		// ❌ Prevent delete if used in bookings
		boolean isUsed = bookingRepo.existsByServiceServiceId(serviceId);

		if (isUsed) {
			throw new RuntimeException("Service cannot be deleted because it is already used in bookings");
		}

		// ✅ Safe delete
		serviceRepo.delete(service);
	}

	public ServiceMaster updateService(Long serviceId, ServiceMaster updatedService) {

		ServiceMaster existingService = serviceRepo.findById(serviceId)
				.orElseThrow(() -> new RuntimeException("Service not found"));

		existingService.setServiceName(updatedService.getServiceName());
		existingService.setPrice(updatedService.getPrice());
		existingService.setDurationHours(updatedService.getDurationHours());

		return serviceRepo.save(existingService);
	}
}
