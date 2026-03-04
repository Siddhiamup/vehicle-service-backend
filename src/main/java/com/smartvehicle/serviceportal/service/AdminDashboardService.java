package com.smartvehicle.serviceportal.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.repository.BookingRepository;
import com.smartvehicle.serviceportal.repository.InvoiceRepository;
import com.smartvehicle.serviceportal.repository.UserRepository;

@Service
public class AdminDashboardService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private InvoiceRepository invoiceRepo;

    // ============================================================
    // ADMIN → DASHBOARD STATS
    // ============================================================
    public Map<String, Object> getDashboardStats(User admin) {

        if (!"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Only admin can access dashboard stats");
        }

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepo.count());
        stats.put("totalBookings", bookingRepo.count());
        stats.put("totalRevenue", invoiceRepo.getTotalRevenue());
        stats.put("pendingPayments",
                invoiceRepo.countByPaymentStatus("PENDING"));

        return stats;
    }
}
