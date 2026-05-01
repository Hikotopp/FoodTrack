package com.foodtrack.spring.springboot_application.entrypoint.sales;

import com.foodtrack.spring.springboot_application.application.model.SalesHistoryView;
import com.foodtrack.spring.springboot_application.domain.model.OrderLine;
import org.springframework.stereotype.Component;

@Component
public class SalesHistoryMapper {

    public SalesHistoryResponse toResponse(SalesHistoryView view) {
        return new SalesHistoryResponse(
                view.id(),
                view.tableId(),
                view.tableNumber(),
                view.createdByUserId(),
                view.createdByName(),
                view.status().name(),
                view.total(),
                view.createdAt(),
                view.updatedAt(),
                view.lines().stream().map(this::toLineResponse).toList()
        );
    }

    private OrderLineHistoryResponse toLineResponse(OrderLine line) {
        return new OrderLineHistoryResponse(
                line.id(),
                line.menuItemId(),
                line.itemName(),
                line.quantity(),
                line.unitPrice(),
                line.subtotal()
        );
    }
}
