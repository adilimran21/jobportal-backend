package com.jobportal.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

        private final String secretKey;

        private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

        public JwtService(
                        @Value("${JWT_SECRET}") String secretKey) {

                this.secretKey = secretKey;
        }

        private SecretKey getSigningKey() {

                return Keys.hmacShaKeyFor(
                                secretKey.getBytes(StandardCharsets.UTF_8));
        }

        public String generateToken(String email, String role) {

                return Jwts.builder()
                                .subject(email)
                                .claim("role", role)
                                .issuedAt(new Date())
                                .expiration(
                                                new Date(
                                                                System.currentTimeMillis()
                                                                                + EXPIRATION_TIME))
                                .signWith(getSigningKey())
                                .compact();
        }

        public String extractEmail(String token) {

                return getClaims(token).getSubject();
        }

        public String extractRole(String token) {

                return getClaims(token).get("role", String.class);
        }

        private Claims getClaims(String token) {

                return Jwts.parser()
                                .verifyWith(getSigningKey())
                                .build()
                                .parseSignedClaims(token)
                                .getPayload();
        }
}