package com.foodtrack.spring.springboot_application.entrypoint.user;

import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
        @NotNull UserRole role
) {
}
