package com.foodtrack.spring.springboot_application.entrypoint.user;

import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import com.foodtrack.spring.springboot_application.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(min = 3, max = 120) String fullName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 64) @ValidPassword String password,
        @NotNull UserRole role
) {
}
