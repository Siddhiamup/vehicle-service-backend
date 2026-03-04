package com.smartvehicle.serviceportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartvehicle.serviceportal.entity.Notification;
import com.smartvehicle.serviceportal.entity.User;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    long countByUserAndReadStatusFalse(User user);
}
