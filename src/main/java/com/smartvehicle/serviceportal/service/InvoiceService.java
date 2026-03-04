package com.smartvehicle.serviceportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartvehicle.serviceportal.entity.Booking;
import com.smartvehicle.serviceportal.entity.Invoice;
import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.repository.InvoiceRepository;
import com.smartvehicle.serviceportal.util.InvoicePdfGenerator;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepo;

    @Autowired
    private NotificationService notificationService;

    // ============================================================
    // GENERATE INVOICE (SERVICE ADVISOR)
    // ============================================================
    public Invoice generateInvoice(Booking booking) {

        if (invoiceRepo.findByBooking(booking).isPresent()) {
            throw new RuntimeException("Invoice already generated");
        }

        Invoice invoice = new Invoice();
        invoice.setBooking(booking);
        invoice.setAmount(booking.getService().getPrice());
        invoice.setPaymentStatus("PENDING");

        Invoice savedInvoice = invoiceRepo.save(invoice);

        // 🔔 NOTIFICATION → CUSTOMER
        notificationService.createNotification(
                booking.getVehicle().getUser(),
                "Invoice generated for your service. Amount: ₹"
                        + invoice.getAmount()
        );

        return savedInvoice;
    }

    // ============================================================
    // GET INVOICE BY BOOKING
    // ============================================================
    public Invoice getInvoiceByBooking(Booking booking) {

        return invoiceRepo.findByBooking(booking)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    // ============================================================
    // MARK AS PAID (ADMIN)
    // ============================================================
    public Invoice markAsPaid(Long invoiceId) {

        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setPaymentStatus("PAID");
        Invoice paidInvoice = invoiceRepo.save(invoice);

        // 🔔 NOTIFICATION → CUSTOMER
        notificationService.createNotification(
                invoice.getBooking().getVehicle().getUser(),
                "Payment received for Invoice #"
                        + invoice.getInvoiceId()
        );

        return paidInvoice;
    }

    // ============================================================
    // CUSTOMER → GET ALL OWN INVOICES
    // ============================================================
    public List<Invoice> getInvoicesForCustomer(User user) {

        if (!"CUSTOMER".equals(user.getRole())) {
            throw new RuntimeException("Only customers can view invoices");
        }

        return invoiceRepo.findByBookingVehicleUser(user);
    }

    // ============================================================
    // DOWNLOAD PDF WITH OWNERSHIP CHECK
    // ============================================================
    public byte[] downloadInvoicePdf(Long invoiceId, User user) {

        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        Booking booking = invoice.getBooking();

        if ("CUSTOMER".equals(user.getRole())) {

            if (!booking.getVehicle().getUser().getUserId()
                    .equals(user.getUserId())) {

                throw new RuntimeException(
                        "You cannot download another user's invoice"
                );
            }
        }

        return InvoicePdfGenerator.generateInvoicePdf(invoice);
    }

    // ============================================================
    // ADMIN → GET ALL INVOICES
    // ============================================================
    public List<Invoice> getAllInvoices(User admin) {

        if (!"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Only admin can view all invoices");
        }

        return invoiceRepo.findAll();
    }
}
