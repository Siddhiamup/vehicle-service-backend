package com.smartvehicle.serviceportal.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // 🔐 Secret key (keep long & random)
    private final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(
                    "mySuperSecretKeyForVehicleServicePortalJWT12345".getBytes()
            );

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    // ====================================================
    // GENERATE TOKEN
    // ====================================================
    public String generateToken(UserDetails userDetails) {

        String role = userDetails.getAuthorities()
                        .stream()
                        .findFirst()
                        .get()
                        .getAuthority();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)   // 👈 ADD THIS
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }


    // ====================================================
    // EXTRACT USERNAME
    // ====================================================
    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ====================================================
    // VALIDATE TOKEN
    // ====================================================
    public boolean validateToken(String token, UserDetails userDetails) {

        String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        Date expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
