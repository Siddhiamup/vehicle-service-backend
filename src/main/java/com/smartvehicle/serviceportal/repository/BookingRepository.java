//package com.smartvehicle.serviceportal.repository;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.smartvehicle.serviceportal.entity.Booking;
//import com.smartvehicle.serviceportal.entity.User;
//
//public interface BookingRepository extends JpaRepository<Booking, Long> {
//
//    // CUSTOMER → booking history (via vehicle → user)
//    List<Booking> findByVehicleUser(User user);
//
//    // Slot availability check
//    int countByBookingDateAndSlot(LocalDate bookingDate, String slot);
//
//    // Booking history by vehicle
//    List<Booking> findByVehicleVehicleId(Long vehicleId);
//}

package com.smartvehicle.serviceportal.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartvehicle.serviceportal.entity.Booking;
import com.smartvehicle.serviceportal.entity.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	// ====================================================
	// EXISTING METHODS (UNCHANGED)
	// ====================================================
	List<Booking> findByVehicleUser(User user);

	int countByBookingDateAndSlot(LocalDate bookingDate, String slot);

	List<Booking> findByVehicleVehicleId(Long vehicleId);

	// ====================================================
	// ✅ NEW — CHECK DUPLICATE BOOKING (Vehicle + Date)
	// ====================================================
	boolean existsByVehicleVehicleIdAndBookingDate(Long vehicleId, LocalDate bookingDate);

	// SERVICE ADVISOR → VIEW DAILY BOOKINGS
	List<Booking> findByBookingDate(LocalDate bookingDate);

	// ============================================================
	// CHECK IF SERVICE IS USED IN ANY BOOKING
	// ============================================================

	boolean existsByServiceServiceId(Long serviceId);
	
	// ============================================================
	// ADMIN → TOTAL BOOKINGS COUNT
	// ============================================================
	long count();

	
	// ============================================================
	// ADMIN → TOTAL BOOKINGS Table
	// ============================================================
	Page<Booking> findByVehicle_VehicleNumberContainingIgnoreCaseOrStatusContainingIgnoreCase(
	        String vehicleNumber,
	        String status,
	        Pageable pageable
	);

}
