package com.smartvehicle.serviceportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.service.AdminDashboardService;
import com.smartvehicle.serviceportal.service.UserService;

@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService adminDashboardService;

    @Autowired
    private UserService userService;

    // ============================================================
    // ADMIN → DASHBOARD STATS
    // ============================================================
    @GetMapping("/stats")
    public ResponseEntity<?> getDashboardStats() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User admin = userService.findByEmail(email);

        return ResponseEntity.ok(
                adminDashboardService.getDashboardStats(admin)
        );
    }
}
