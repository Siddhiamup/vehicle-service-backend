package com.smartvehicle.serviceportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartvehicle.serviceportal.entity.Vehicle;
import com.smartvehicle.serviceportal.entity.User;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // ====================================================
    // GET ALL VEHICLES FOR A USER
    // ====================================================
    List<Vehicle> findByUser(User user);
}
