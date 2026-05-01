package com.foodtrack.spring.springboot_application.application.model;

import com.foodtrack.spring.springboot_application.domain.model.UserRole;

public record UserProfileView(
        Long id,
        String fullName,
        String email,
        UserRole role
) {
}
