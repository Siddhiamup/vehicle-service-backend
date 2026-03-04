package com.smartvehicle.serviceportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// ============================================================
	// REGISTER / SAVE USER
	// ============================================================
	public User saveUser(User user) {

		// Encode password ONCE
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userRepo.save(user);
	}

	// ============================================================
	// FIND BY EMAIL
	// ============================================================
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

//	public Page<User> getUsersPaginated(Pageable pageable) {
//		return userRepo.findAll(pageable);
//	}

	public Page<User> getUsersPaginated(String search, Pageable pageable) {

		if (search == null || search.trim().isEmpty()) {
			return userRepo.findAll(pageable);
		}

		return userRepo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
	}
}
