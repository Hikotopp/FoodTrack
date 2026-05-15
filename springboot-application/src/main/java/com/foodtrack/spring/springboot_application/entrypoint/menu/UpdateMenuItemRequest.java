package com.foodtrack.spring.springboot_application.entrypoint.menu;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateMenuItemRequest(
        @NotBlank @Size(min = 2, max = 150) String name,
        @NotBlank @Size(min = 2, max = 500) String description,
        @NotBlank @Size(max = 30) String category,
        @NotNull @DecimalMin(value = "0.01") BigDecimal price,
        @Min(0) int stockQuantity,
        boolean active
) {}
