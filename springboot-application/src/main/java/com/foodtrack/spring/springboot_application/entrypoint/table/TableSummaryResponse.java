package com.foodtrack.spring.springboot_application.entrypoint.table;

import java.math.BigDecimal;

public record TableSummaryResponse(
        Long id,
        int tableNumber,
        String status,
        BigDecimal total,
        int itemCount
) {}