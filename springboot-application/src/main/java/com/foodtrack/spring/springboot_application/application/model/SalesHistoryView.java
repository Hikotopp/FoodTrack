package com.foodtrack.spring.springboot_application.application.model;

import com.foodtrack.spring.springboot_application.domain.model.OrderLine;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SalesHistoryView(
        Long id,
        Long tableId,
        Integer tableNumber,
        Long createdByUserId,
        String createdByName,
        OrderStatus status,
        BigDecimal total,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderLine> lines
) {
}
