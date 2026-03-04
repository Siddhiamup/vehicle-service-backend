package com.smartvehicle.serviceportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartvehicle.serviceportal.entity.Notification;
import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    // ============================================================
    // CREATE NOTIFICATION
    // ============================================================
    public void createNotification(User user, String message) {

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setReadStatus(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepo.save(notification);
    }

    // ============================================================
    // GET USER NOTIFICATIONS
    // ============================================================
    public List<Notification> getMyNotifications(User user) {
        return notificationRepo.findByUserOrderByCreatedAtDesc(user);
    }

    // ============================================================
    // MARK AS READ
    // ============================================================
    public void markAsRead(Long notificationId) {

        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setReadStatus(true);
        notificationRepo.save(notification);
    }

    // ============================================================
    // UNREAD COUNT
    // ============================================================
    public long getUnreadCount(User user) {
        return notificationRepo.countByUserAndReadStatusFalse(user);
    }
}
