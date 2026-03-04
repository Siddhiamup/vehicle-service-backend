package com.smartvehicle.serviceportal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    // Who receives the notification
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String message;

    private boolean readStatus; // false = unread

    private LocalDateTime createdAt;
}
