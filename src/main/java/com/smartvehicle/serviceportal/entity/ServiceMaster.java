package com.smartvehicle.serviceportal.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "services")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    private String serviceName;
    private Integer durationHours;
    private Double price;


}
