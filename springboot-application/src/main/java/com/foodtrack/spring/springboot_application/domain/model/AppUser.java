package com.foodtrack.spring.springboot_application.domain.model;

public record AppUser(
        Long id,
        String fullName,
        String email,
        String passwordHash,
        UserRole role
) {
}
