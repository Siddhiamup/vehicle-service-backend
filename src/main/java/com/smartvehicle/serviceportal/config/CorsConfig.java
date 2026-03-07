//package com.smartvehicle.serviceportal.config;
//
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//@Configuration
//public class CorsConfig {
//
//    // ============================================================
//    // GLOBAL CORS CONFIGURATION
//    // Fixes React (Vite) → Spring Boot CORS issues
//    // Especially for PATCH / PUT / DELETE requests
//    // ============================================================
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration config = new CorsConfiguration();
//
//        // ----------------------------------------------------
//        // Frontend origin (React + Vite)
//        // ----------------------------------------------------
//        config.setAllowedOrigins(
//                List.of("http://localhost:5173")
//        );
//
//        // ----------------------------------------------------
//        // Allowed HTTP Methods
//        // PATCH is CRITICAL for status updates
//        // ----------------------------------------------------
//        config.setAllowedMethods(List.of(
//                "GET",
//                "POST",
//                "PUT",
//                "PATCH",
//                "DELETE",
//                "OPTIONS"
//        ));
//
//        // ----------------------------------------------------
//        // Allow all headers (Authorization, Content-Type, etc.)
//        // ----------------------------------------------------
//        config.setAllowedHeaders(List.of("*"));
//
//        // ----------------------------------------------------
//        // Allow cookies / Authorization header
//        // ----------------------------------------------------
//        config.setAllowCredentials(true);
//
//        // ----------------------------------------------------
//        // Apply this CORS config to all endpoints
//        // ----------------------------------------------------
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }
//}


package com.smartvehicle.serviceportal.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ============================================================
        // Allowed Frontend Origins
        // ============================================================
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",                    // local development
                "https://vehicle-service-frontend.vercel.app" // production frontend
        ));

        // ============================================================
        // Allowed HTTP Methods
        // ============================================================
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // ============================================================
        // Allowed Headers
        // ============================================================
        config.setAllowedHeaders(List.of("*"));

        // ============================================================
        // Allow Authorization Header (JWT)
        // ============================================================
        config.setAllowCredentials(true);

        // ============================================================
        // Apply to all routes
        // ============================================================
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
