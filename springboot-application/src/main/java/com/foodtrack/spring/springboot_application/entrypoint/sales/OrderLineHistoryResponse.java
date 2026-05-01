package com.foodtrack.spring.springboot_application.entrypoint.sales;

import java.math.BigDecimal;

public record OrderLineHistoryResponse(
        Long id,
        Long menuItemId,
        String itemName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
