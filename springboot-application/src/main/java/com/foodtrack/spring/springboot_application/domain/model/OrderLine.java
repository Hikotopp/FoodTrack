package com.foodtrack.spring.springboot_application.domain.model;

import java.math.BigDecimal;

public record OrderLine(
        Long id,
        Long menuItemId,
        String itemName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
