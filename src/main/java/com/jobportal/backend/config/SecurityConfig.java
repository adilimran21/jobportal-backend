package com.jobportal.backend.config;

import com.jobportal.backend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(
                        JwtAuthenticationFilter jwtAuthenticationFilter) {

                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public UserDetailsService userDetailsService() {
                return username -> {
                        throw new UsernameNotFoundException("User not found");
                };
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(
                                List.of("http://localhost:5173"));

                configuration.setAllowedMethods(
                                List.of(
                                                "GET",
                                                "POST",
                                                "PUT",
                                                "PATCH",
                                                "DELETE",
                                                "OPTIONS"));

                configuration.setAllowedHeaders(
                                List.of(
                                                "Authorization",
                                                "Content-Type"));

                configuration.setAllowCredentials(false);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration(
                                "/**",
                                configuration);

                return source;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .cors(Customizer.withDefaults())

                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth

                                                // CORS preflight
                                                .requestMatchers(
                                                                HttpMethod.OPTIONS,
                                                                "/**")
                                                .permitAll()

                                                // Public APIs
                                                .requestMatchers(
                                                                "/api/users/register",
                                                                "/api/users/login",
                                                                "/api/users/forgot-password",
                                                                "/api/users/verify-otp",
                                                                "/api/users/reset-password",
                                                                "/api/jobseeker/jobs/**",
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

                                                // User profile - authenticated
                                                .requestMatchers(
                                                                "/api/users/profile")
                                                .authenticated()

                                                // Admin APIs
                                                .requestMatchers(
                                                                "/api/admin/**")
                                                .hasRole("ADMIN")

                                                // Candidate APIs
                                                .requestMatchers(
                                                                "/api/candidate/**")
                                                .hasRole("JOB_SEEKER")

                                                // Recruiter APIs
                                                .requestMatchers(
                                                                "/api/recruiter/**")
                                                .hasRole("RECRUITER")

                                                // Everything else
                                                .anyRequest()
                                                .authenticated())

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}