package com.foodtrack.spring.springboot_application.entrypoint.auth;

public record AuthenticationResponse(
        String token,
        String fullName,
        String email,
        String role
) {}