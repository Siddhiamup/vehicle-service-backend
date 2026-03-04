package com.smartvehicle.serviceportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartvehicle.serviceportal.entity.ServiceMaster;

public interface ServiceRepository extends JpaRepository<ServiceMaster, Long> {
}
