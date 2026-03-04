package com.smartvehicle.serviceportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartvehicle.serviceportal.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// ============================================================
	// ADMIN → TOTAL USERS COUNT
	// ============================================================
	long count();

	User findByEmail(String email);

	Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

}
