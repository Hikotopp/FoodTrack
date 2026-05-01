package com.foodtrack.spring.springboot_application.domain.model;

import java.math.BigDecimal;

public record MenuItem(
        Long id,
        String name,
        String description,
        MenuCategory category,
        BigDecimal price,
        boolean active
) {
}
