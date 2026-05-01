package com.foodtrack.spring.springboot_application.application.model;

import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import com.foodtrack.spring.springboot_application.domain.model.RestaurantTable;

import java.util.List;

public record TableDashboardView(
        RestaurantTable table,
        CustomerOrder currentOrder,
        List<MenuItem> menuItems
) {
}
