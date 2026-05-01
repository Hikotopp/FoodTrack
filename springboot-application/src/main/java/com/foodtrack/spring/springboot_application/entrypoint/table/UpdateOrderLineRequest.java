package com.foodtrack.spring.springboot_application.entrypoint.table;

import jakarta.validation.constraints.Min;

public record UpdateOrderLineRequest(
        @Min(1) int quantity
) {
}
