package com.foodtrack.spring.springboot_application.entrypoint.table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddOrderLineRequest(
        @NotNull Long menuItemId,
        @Min(1) int quantity
) {
}
