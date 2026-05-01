package com.foodtrack.spring.springboot_application.application.port.in;

import com.foodtrack.spring.springboot_application.application.model.TableDashboardView;
import com.foodtrack.spring.springboot_application.application.model.TableSummaryView;
import com.foodtrack.spring.springboot_application.domain.model.TableStatus;

import java.util.List;

public interface TableUseCase {
    List<TableSummaryView> listTables();
    TableDashboardView getDashboard(Long tableId);
    TableSummaryView createTable(int tableNumber);
    void deleteTable(Long tableId);
    TableSummaryView updateTableStatus(Long tableId, TableStatus status);
    TableDashboardView addOrderLine(Long tableId, Long menuItemId, int quantity, String currentUserEmail);
    TableDashboardView updateOrderLine(Long tableId, Long lineId, int quantity);
    TableDashboardView removeOrderLine(Long tableId, Long lineId);
    TableDashboardView closeOrder(Long tableId);
}
