package com.smartvehicle.serviceportal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.smartvehicle.serviceportal.entity.User;
import com.smartvehicle.serviceportal.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdminUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            // ==========================================================
            // CREATE ADMIN IF NOT EXISTS
            // ==========================================================
            if (userRepository.findByEmail("admin@gmail.com") == null) {

                User admin = new User();

                admin.setName("System Admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println("Admin user created");
            }

            // ==========================================================
            // CREATE SERVICE ADVISOR IF NOT EXISTS
            // ==========================================================
            if (userRepository.findByEmail("advisor@gmail.com") == null) {

                User advisor = new User();

                advisor.setName("Service Advisor");
                advisor.setEmail("advisor@gmail.com");
                advisor.setPassword(passwordEncoder.encode("advisor123"));
                advisor.setRole("SERVICE_ADVISOR");

                userRepository.save(advisor);

                System.out.println("Service Advisor created");
            }

        };
    }
}