package com.foodtrack.spring.springboot_application.application.port.in;

import com.foodtrack.spring.springboot_application.application.model.TableSummaryView;
import com.foodtrack.spring.springboot_application.domain.model.TableStatus;

public interface TableAdministrationUseCase {
    TableSummaryView createTable(int tableNumber);
    void deleteTable(Long tableId);
    TableSummaryView updateTableStatus(Long tableId, TableStatus status);
}
