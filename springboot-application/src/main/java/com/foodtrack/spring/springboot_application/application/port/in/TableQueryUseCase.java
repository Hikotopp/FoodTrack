package com.foodtrack.spring.springboot_application.application.port.in;

import com.foodtrack.spring.springboot_application.application.model.TableDashboardView;
import com.foodtrack.spring.springboot_application.application.model.TableSummaryView;

import java.util.List;

public interface TableQueryUseCase {
    List<TableSummaryView> listTables();
    TableDashboardView getDashboard(Long tableId);
}
