package com.foodtrack.spring.springboot_application.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CustomerOrder(
        Long id,
        Long tableId,
        Long createdByUserId,
        OrderStatus status,
        BigDecimal total,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderLine> lines
) {
}
