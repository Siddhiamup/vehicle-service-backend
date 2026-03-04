package com.smartvehicle.serviceportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartvehicle.serviceportal.entity.ServiceMaster;
import com.smartvehicle.serviceportal.service.ServiceMasterService;

@RestController
@RequestMapping("/services")
public class ServiceMasterController {

    @Autowired
    private ServiceMasterService serviceMasterService;

    // ============================================================
    // ADMIN → ADD SERVICE
    // ============================================================

    @PostMapping("/add")
    public ResponseEntity<?> addService(
            @RequestBody ServiceMaster serviceMaster) {

        return ResponseEntity.ok(
                serviceMasterService.addService(serviceMaster)
        );
    }

    // ============================================================
    // PUBLIC → VIEW ALL SERVICES
    // ============================================================

    @GetMapping("/all")
    public ResponseEntity<?> getAllServices() {
        return ResponseEntity.ok(
                serviceMasterService.getAllServices()
        );
    }

    // ============================================================
    // ADMIN → DELETE SERVICE (SAFE)
    // ============================================================

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<?> deleteService(
            @PathVariable Long serviceId) {

        try {
            serviceMasterService.deleteService(serviceId);
            return ResponseEntity.ok("Service deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
