//package com.smartvehicle.serviceportal.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.smartvehicle.serviceportal.security.JwtFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    @Autowired
//    private JwtFilter jwtFilter;
//
//    // ------------------------------------------------------------
//    // PASSWORD ENCODER
//    // ------------------------------------------------------------
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // ------------------------------------------------------------
//    // AUTH MANAGER
//    // ------------------------------------------------------------
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    // ------------------------------------------------------------
//    // SECURITY FILTER CHAIN
//    // ------------------------------------------------------------
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//            .cors(cors -> {}) // uses CorsConfig
//            .csrf(csrf -> csrf.disable())
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//
//            .authorizeHttpRequests(auth -> auth
//
//                // PUBLIC
//                .requestMatchers(
//                    "/auth/login",
//                    "/auth/register",
//                    "/services/all"
//                ).permitAll()
//
//                // CUSTOMER
//                .requestMatchers(
//                    "/vehicles/**",
//                    "/bookings/create/**",
//                    "/bookings/history/**",
//                    "/bookings/cancel/**",
//                    "/invoices/view/**",
//                    "/invoices/download/**"
//                ).hasAuthority("ROLE_CUSTOMER")
//
//                // SERVICE ADVISOR
//                .requestMatchers(
//                    "/bookings/updateStatus/**",
//                    "/bookings/dashboard/**",
//                    "/invoices/generate/**"
//                ).hasAuthority("ROLE_SERVICE_ADVISOR")
//
//                // ADMIN
//                .requestMatchers(
//                    "/services/**",
//                    "/invoices/pay/**"
//                ).hasAuthority("ROLE_ADMIN")
//
//                .anyRequest().authenticated()
//                
//             // ============================================================
//             // NOTIFICATIONS (ALL ROLES)
//             // ============================================================
//             .requestMatchers(
//                 "/notifications/**"
//             ).authenticated()
//
//            )
//
//            .addFilterBefore(
//                jwtFilter,
//                UsernamePasswordAuthenticationFilter.class
//            );
//
//        return http.build();
//    }
//}

package com.smartvehicle.serviceportal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.smartvehicle.serviceportal.security.JwtFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;

	// ============================================================
	// PASSWORD ENCODER
	// ============================================================
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// ============================================================
	// AUTHENTICATION MANAGER
	// ============================================================
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

		return config.getAuthenticationManager();
	}

	// ============================================================
	// SECURITY FILTER CHAIN (FIXED ORDER)
	// ============================================================
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				// Enable CORS (uses CorsConfig)
				.cors(cors -> {
				})

				// Disable CSRF (JWT based)
				.csrf(csrf -> csrf.disable())

				// Stateless session
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// ====================================================
				// AUTHORIZATION RULES (ORDER MATTERS!)
				// ====================================================
				.authorizeHttpRequests(auth -> auth
						// Allow CORS preflight
						.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
						// ---------- PUBLIC ----------
						.requestMatchers("/auth/login", "/auth/register", "/services/all").permitAll()

						// ---------- CUSTOMER ----------
						.requestMatchers("/vehicles/**", "/bookings/create/**", "/bookings/history/**",
								"/bookings/cancel/**", "/invoices/view/**", "/invoices/download/**", "/invoices/my",
								"/notifications/**")
						.hasAuthority("ROLE_CUSTOMER")

						// ---------- SERVICE ADVISOR ----------
						.requestMatchers("/bookings/updateStatus/**", "/bookings/dashboard/**", "/invoices/generate/**",
								"/notifications/**")
						.hasAuthority("ROLE_SERVICE_ADVISOR")

						// ---------- ADMIN ----------
						.requestMatchers("/services/**", "/invoices/pay/**", "/invoices/all", "/notifications/**",
								"/users/all", "/bookings/admin/**")
						.hasAuthority("ROLE_ADMIN")

						// ---------- FALLBACK (LAST LINE ONLY) ----------
						.anyRequest().authenticated())

				// JWT Filter
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
