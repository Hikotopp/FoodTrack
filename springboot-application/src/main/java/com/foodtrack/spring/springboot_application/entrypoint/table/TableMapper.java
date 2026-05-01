package com.foodtrack.spring.springboot_application.entrypoint.table;

import com.foodtrack.spring.springboot_application.application.model.TableDashboardView;
import com.foodtrack.spring.springboot_application.application.model.TableSummaryView;
import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;
import com.foodtrack.spring.springboot_application.domain.model.OrderLine;
import com.foodtrack.spring.springboot_application.entrypoint.menu.MenuItemRestMapper;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableMapper {


private final MenuItemRestMapper menuItemMapper;

public TableMapper(MenuItemRestMapper menuItemMapper) {
    this.menuItemMapper = menuItemMapper;
}
    public TableSummaryResponse toSummaryResponse(TableSummaryView view) {
        return new TableSummaryResponse(
                view.id(),
                view.tableNumber(),
                view.status().name(),
                view.total(),
                view.itemCount()
        );
    }

    public TableDashboardResponse toDashboardResponse(TableDashboardView view) {
        TableSummaryResponse tableSummary = new TableSummaryResponse(
                view.table().id(),
                view.table().tableNumber(),
                view.table().status().name(),
                view.currentOrder() != null ? view.currentOrder().total() : BigDecimal.ZERO,
                view.currentOrder() != null ? view.currentOrder().lines().stream().mapToInt(OrderLine::quantity).sum() : 0
        );

        CustomerOrderResponse orderResponse = view.currentOrder() != null ? toCustomerOrderResponse(view.currentOrder()) : null;

        return new TableDashboardResponse(
                tableSummary,
                orderResponse,
                view.menuItems().stream()
                        .map(menuItemMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }

    private CustomerOrderResponse toCustomerOrderResponse(CustomerOrder order) {
        List<OrderLineResponse> lineResponses = order.lines().stream()
                .map(this::toOrderLineResponse)
                .collect(Collectors.toList());

        return new CustomerOrderResponse(
                order.id(),
                order.tableId(),
                order.status().name(),
                order.total(),
                order.createdAt(),
                order.updatedAt(),
                lineResponses
        );
    }

    private OrderLineResponse toOrderLineResponse(OrderLine line) {
        return new OrderLineResponse(
                line.id(),
                line.menuItemId(),
                line.itemName(),
                line.quantity(),
                line.unitPrice(),
                line.subtotal()
        );
    }
}