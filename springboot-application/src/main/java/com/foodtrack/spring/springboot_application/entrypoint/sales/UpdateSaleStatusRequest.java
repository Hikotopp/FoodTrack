package com.foodtrack.spring.springboot_application.entrypoint.sales;

import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateSaleStatusRequest(
        @NotNull OrderStatus status
) {
}
