package com.smartvehicle.serviceportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    private String vehicleNumber;

    private String model;

    private String fuelType;

    private int year;

    // ====================================================
    // MANY VEHICLES → ONE USER
    // ====================================================
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
