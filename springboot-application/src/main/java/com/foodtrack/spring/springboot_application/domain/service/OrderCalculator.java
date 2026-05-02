package com.foodtrack.spring.springboot_application.domain.service;

import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import com.foodtrack.spring.springboot_application.domain.model.OrderLine;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderCalculator {

    public List<OrderLine> mergeOrderLine(List<OrderLine> currentLines, MenuItem menuItem, int quantity) {
        List<OrderLine> mergedLines = new ArrayList<>();
        boolean merged = false;

        for (OrderLine line : currentLines) {
            if (line.menuItemId().equals(menuItem.id())) {
                int newQuantity = line.quantity() + quantity;
                mergedLines.add(new OrderLine(
                        line.id(),
                        line.menuItemId(),
                        line.itemName(),
                        newQuantity,
                        line.unitPrice(),
                        calculateSubtotal(line.unitPrice(), newQuantity)
                ));
                merged = true;
            } else {
                mergedLines.add(line);
            }
        }

        if (!merged) {
            mergedLines.add(new OrderLine(
                    null,
                    menuItem.id(),
                    menuItem.name(),
                    quantity,
                    menuItem.price(),
                    calculateSubtotal(menuItem.price(), quantity)
            ));
        }

        return mergedLines;
    }

    public List<OrderLine> updateQuantity(List<OrderLine> lines, Long lineId, int quantity) {
        return lines.stream()
                .map(line -> line.id().equals(lineId)
                        ? new OrderLine(
                        line.id(),
                        line.menuItemId(),
                        line.itemName(),
                        quantity,
                        line.unitPrice(),
                        calculateSubtotal(line.unitPrice(), quantity)
                )
                        : line)
                .toList();
    }

    public CustomerOrder recalculate(CustomerOrder order, List<OrderLine> lines, OrderStatus status) {
        BigDecimal total = lines.stream()
                .map(OrderLine::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CustomerOrder(
                order.id(),
                order.tableId(),
                order.createdByUserId(),
                status,
                total,
                order.createdAt(),
                LocalDateTime.now(),
                lines
        );
    }

    private BigDecimal calculateSubtotal(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
