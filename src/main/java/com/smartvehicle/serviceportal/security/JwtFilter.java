package com.smartvehicle.serviceportal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    // ============================================================
    // JWT FILTER LOGIC
    // ============================================================

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // ========================================================
        // STEP 1: Ignore Public Auth Endpoints
        // ========================================================

        String path = request.getServletPath();

        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ========================================================
        // STEP 2: Extract Authorization Header
        // ========================================================

        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        // ========================================================
        // STEP 3: Extract Token
        // ========================================================

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            try {
                username = jwtUtil.extractUsername(token);
            }
            catch (JwtException e) {
                // Invalid or expired token → skip authentication
                System.out.println("Invalid JWT Token: " + e.getMessage());
            }
        }

        // ========================================================
        // STEP 4: Validate Token & Set Authentication
        // ========================================================

        if (username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("Authenticated user: " + username);
                System.out.println("Authorities: " + userDetails.getAuthorities());
            }
        }

        // ========================================================
        // Continue Filter Chain
        // ========================================================

        filterChain.doFilter(request, response);
    }
}
