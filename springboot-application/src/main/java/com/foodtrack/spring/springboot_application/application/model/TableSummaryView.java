package com.foodtrack.spring.springboot_application.application.model;

import com.foodtrack.spring.springboot_application.domain.model.TableStatus;

import java.math.BigDecimal;

public record TableSummaryView(
        Long id,
        int tableNumber,
        TableStatus status,
        BigDecimal total,
        int itemCount
) {
}
