package com.foodtrack.spring.springboot_application.entrypoint.user;

public record UserAccountResponse(
        Long id,
        String fullName,
        String email,
        String role
) {
}
