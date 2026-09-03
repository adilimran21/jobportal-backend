package com.jobportal.backend.security;

import com.jobportal.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter
                extends OncePerRequestFilter {

        private final JwtService jwtService;

        public JwtAuthenticationFilter(JwtService jwtService) {
                this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {

                String authHeader = request.getHeader("Authorization");

                if (authHeader == null ||
                                !authHeader.startsWith("Bearer ")) {

                        filterChain.doFilter(request, response);
                        return;
                }

                String token = authHeader.substring(7);

                try {

                        String email = jwtService.extractEmail(token);

                        String role = jwtService.extractRole(token);

                        System.out.println("JWT EMAIL: " + email);
                        System.out.println("JWT ROLE: " + role);

                        if (email != null &&
                                        role != null &&
                                        SecurityContextHolder
                                                        .getContext()
                                                        .getAuthentication() == null) {

                                String authorityRole = role;

                                if (!authorityRole.startsWith("ROLE_")) {
                                        authorityRole = "ROLE_" + authorityRole;
                                }

                                System.out.println(
                                                "SPRING AUTHORITY: " + authorityRole);

                                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                                                authorityRole);

                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                                email,
                                                null,
                                                Collections.singletonList(authority));

                                SecurityContextHolder
                                                .getContext()
                                                .setAuthentication(authentication);

                                System.out.println(
                                                "AUTHENTICATION SET SUCCESSFULLY");
                        }

                } catch (Exception e) {

                        System.out.println(
                                        "JWT Error: " + e.getMessage());
                }

                filterChain.doFilter(request, response);
        }
}