package com.foodtrack.spring.springboot_application.entrypoint.sales;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SalesHistoryResponse(
        Long id,
        Long tableId,
        Integer tableNumber,
        Long createdByUserId,
        String createdByName,
        String status,
        BigDecimal total,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderLineHistoryResponse> lines
) {
}
