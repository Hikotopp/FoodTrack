package com.foodtrack.spring.springboot_application.domain.model;

public record RestaurantTable(
        Long id,
        int tableNumber,
        TableStatus status
) {
}
