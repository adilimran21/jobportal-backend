package com.jobportal.backend.config;

import com.jobportal.backend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(
                        JwtAuthenticationFilter jwtAuthenticationFilter) {

                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth

                                                // Public APIs
                                                .requestMatchers(
                                                                "/api/users/register",
                                                                "/api/users/login",
                                                                "/api/jobseeker/jobs",
                                                                "/api/jobs/search",
                                                                "/api/companies",
                                                                "/api/companies/**")
                                                .permitAll()

                                                // Categories - public GET
                                                .requestMatchers(
                                                                HttpMethod.GET,
                                                                "/api/categories",
                                                                "/api/categories/**")
                                                .permitAll()

                                                // Categories - RECRUITER only
                                                .requestMatchers(
                                                                HttpMethod.POST,
                                                                "/api/categories",
                                                                "/api/categories/**")
                                                .hasRole("RECRUITER")

                                                .requestMatchers(
                                                                HttpMethod.DELETE,
                                                                "/api/categories",
                                                                "/api/categories/**")
                                                .hasRole("RECRUITER")

                                                // User profile - any authenticated user
                                                .requestMatchers(
                                                                "/api/users/profile")
                                                .authenticated()

                                                // Candidate APIs - JOB_SEEKER only
                                                .requestMatchers(
                                                                "/api/candidate/**")
                                                .hasRole("JOB_SEEKER")

                                                // Recruiter APIs - RECRUITER only
                                                .requestMatchers(
                                                                "/api/recruiter/**")
                                                .hasRole("RECRUITER")

                                                // Everything else requires authentication
                                                .anyRequest().authenticated())

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}