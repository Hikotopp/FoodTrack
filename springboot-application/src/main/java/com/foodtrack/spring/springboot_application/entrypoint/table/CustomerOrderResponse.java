package com.foodtrack.spring.springboot_application.entrypoint.table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CustomerOrderResponse(
        Long id,
        Long tableId,
        String status,
        BigDecimal total,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderLineResponse> lines
) {}