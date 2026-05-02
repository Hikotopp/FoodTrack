package com.foodtrack.spring.springboot_application.application.port.in;

import com.foodtrack.spring.springboot_application.application.model.TableDashboardView;

public interface OrderUseCase {
    TableDashboardView addOrderLine(Long tableId, Long menuItemId, int quantity, String currentUserEmail);
    TableDashboardView updateOrderLine(Long tableId, Long lineId, int quantity);
    TableDashboardView removeOrderLine(Long tableId, Long lineId);
    TableDashboardView closeOrder(Long tableId);
}
