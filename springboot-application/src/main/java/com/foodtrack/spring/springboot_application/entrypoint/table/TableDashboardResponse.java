package com.foodtrack.spring.springboot_application.entrypoint.table;

import com.foodtrack.spring.springboot_application.entrypoint.menu.MenuItemResponse;
import java.util.List;

public record TableDashboardResponse(
        TableSummaryResponse table,
        CustomerOrderResponse currentOrder,
        List<MenuItemResponse> menuItems
) {}