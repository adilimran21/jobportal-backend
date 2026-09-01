package com.jobportal.backend.controller;

import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.service.JwtService;
import com.jobportal.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody UserEntity user) {

        try {

            System.out.println("Name : " + user.getName());
            System.out.println("Email : " + user.getEmail());
            System.out.println("Role : " + user.getRole());

            UserEntity savedUser =
                    userService.createUser(user);

            return ResponseEntity
                    .ok(savedUser);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody UserEntity user) {

        String token = userService.loginUser(
                user.getEmail(),
                user.getPassword()
        );

        if (token != null) {

            return ResponseEntity.ok(token);
        }

        return ResponseEntity
                .status(401)
                .body("Invalid email or password");
    }

    // =========================
    // PROFILE
    // =========================
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            @RequestHeader(value = "Authorization",
                    required = false) String authHeader) {

        // Check Authorization header
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        try {

            String email =
                    jwtService.extractEmail(token);

            UserEntity user =
                    userService.getUserByEmail(email);

            if (user != null) {

                return ResponseEntity.ok(user);
            }

            return ResponseEntity
                    .status(404)
                    .body("User not found");

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid or expired token");
        }
    }
}