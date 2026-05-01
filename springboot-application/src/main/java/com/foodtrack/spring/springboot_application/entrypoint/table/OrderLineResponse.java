package com.foodtrack.spring.springboot_application.entrypoint.table;

import java.math.BigDecimal;

public record OrderLineResponse(
        Long id,
        Long menuItemId,
        String itemName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}