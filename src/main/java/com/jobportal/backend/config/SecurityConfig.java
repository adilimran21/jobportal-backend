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

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

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

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        
                        // CORS PREFLIGHT
                        
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**")
                        .permitAll()

                        
                        // PUBLIC APIs
                        
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

                        
                        // CATEGORIES
                        
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categories",
                                "/api/categories/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/categories",
                                "/api/categories/**")
                        .hasAuthority("ROLE_RECRUITER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/categories",
                                "/api/categories/**")
                        .hasAuthority("ROLE_RECRUITER")

                        
                        // USER PROFILE
                        
                        .requestMatchers(
                                "/api/users/profile")
                        .authenticated()

                        
                        // ADMIN APIs
                        
                        .requestMatchers(
                                "/api/admin/**")
                        .hasAuthority("ROLE_ADMIN")

                        
                        // CANDIDATE APIs
                        
                        .requestMatchers(
                                "/api/candidate/**")
                        .hasAuthority("ROLE_JOB_SEEKER")

                        
                        // RECRUITER APIs
                        
                        .requestMatchers(
                                "/api/recruiter/**")
                        .hasAuthority("ROLE_RECRUITER")

                        
                        // EVERYTHING ELSE
                        
                        .anyRequest()
                        .authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}