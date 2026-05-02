package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.TableDashboardView;
import com.foodtrack.spring.springboot_application.application.model.TableSummaryView;
import com.foodtrack.spring.springboot_application.application.port.in.TableUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.CustomerOrderRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.MenuItemRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.RestaurantTableRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import com.foodtrack.spring.springboot_application.domain.model.OrderLine;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;
import com.foodtrack.spring.springboot_application.domain.model.RestaurantTable;
import com.foodtrack.spring.springboot_application.domain.model.TableStatus;
import com.foodtrack.spring.springboot_application.domain.service.OrderCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Transactional
public class RestaurantTableApplicationService implements TableUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantTableApplicationService.class);

    private final RestaurantTableRepositoryPort restaurantTableRepositoryPort;
    private final CustomerOrderRepositoryPort customerOrderRepositoryPort;
    private final MenuItemRepositoryPort menuItemRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final OrderCalculator orderCalculator;

    public RestaurantTableApplicationService(
            RestaurantTableRepositoryPort restaurantTableRepositoryPort,
            CustomerOrderRepositoryPort customerOrderRepositoryPort,
            MenuItemRepositoryPort menuItemRepositoryPort,
            UserRepositoryPort userRepositoryPort,
            OrderCalculator orderCalculator
    ) {
        this.restaurantTableRepositoryPort = restaurantTableRepositoryPort;
        this.customerOrderRepositoryPort = customerOrderRepositoryPort;
        this.menuItemRepositoryPort = menuItemRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.orderCalculator = orderCalculator;
    }

    @Override
    public List<TableSummaryView> listTables() {
        logger.debug("Listing all tables with open orders summary");
        List<RestaurantTable> tables = restaurantTableRepositoryPort.findAll().stream()
                .sorted(Comparator.comparingInt(RestaurantTable::tableNumber))
                .toList();

        Map<Long, CustomerOrder> openOrders = customerOrderRepositoryPort.findOpenByTableIds(
                tables.stream().map(RestaurantTable::id).toList()
        );

        return tables.stream()
                .map(table -> toSummary(table, openOrders.get(table.id())))
                .toList();
    }

    @Override
    public TableDashboardView getDashboard(Long tableId) {
        logger.debug("Fetching dashboard for table id: {}", tableId);
        RestaurantTable table = getTable(tableId);
        CustomerOrder currentOrder = customerOrderRepositoryPort.findOpenByTableId(tableId).orElse(null);
        return new TableDashboardView(table, currentOrder, getSortedMenu());
    }

    @Override
    public TableSummaryView createTable(int tableNumber) {
        logger.info("Attempting to create table with number: {}", tableNumber);
        restaurantTableRepositoryPort.findByTableNumber(tableNumber).ifPresent(existing -> {
            logger.warn("Table number {} already exists", tableNumber);
            throw new BusinessRuleException("Table number already exists.");
        });

        RestaurantTable savedTable = restaurantTableRepositoryPort.save(
                new RestaurantTable(null, tableNumber, TableStatus.AVAILABLE)
        );
        logger.info("Table created with id: {} and number: {}", savedTable.id(), tableNumber);
        return toSummary(savedTable, null);
    }

    @Override
    public void deleteTable(Long tableId) {
        logger.info("Attempting to delete table with id: {}", tableId);
        customerOrderRepositoryPort.findOpenByTableId(tableId).ifPresent(order -> {
            logger.warn("Cannot delete table {} because it has an open order id: {}", tableId, order.id());
            throw new BusinessRuleException("Cannot delete a table with an open order.");
        });

        getTable(tableId);
        if (tableId != null) {
            restaurantTableRepositoryPort.deleteById(tableId);
        }
        logger.info("Table {} deleted successfully", tableId);
    }

    @Override
    public TableSummaryView updateTableStatus(Long tableId, TableStatus status) {
        logger.info("Updating table {} status to {}", tableId, status);
        RestaurantTable table = getTable(tableId);
        CustomerOrder openOrder = customerOrderRepositoryPort.findOpenByTableId(tableId).orElse(null);

        if (status == TableStatus.AVAILABLE && openOrder != null && !openOrder.lines().isEmpty()) {
            logger.warn("Cannot set table {} as available because it has an open order with lines", tableId);
            throw new BusinessRuleException("Close the current order before setting the table as available.");
        }

        RestaurantTable updatedTable = restaurantTableRepositoryPort.save(
                new RestaurantTable(table.id(), table.tableNumber(), status)
        );
        logger.info("Table {} status updated to {}", tableId, status);
        return toSummary(updatedTable, openOrder);
    }

    @Override
    public TableDashboardView addOrderLine(Long tableId, Long menuItemId, int quantity, String currentUserEmail) {
        logger.info("Adding order line: tableId={}, menuItemId={}, quantity={}, user={}", tableId, menuItemId, quantity, currentUserEmail);
        RestaurantTable table = getTable(tableId);
        if (menuItemId == null) {
            throw new IllegalArgumentException("Menu item ID cannot be null");
        }
        MenuItem menuItem = menuItemRepositoryPort.findById(menuItemId)
                .orElseThrow(() -> {
                    logger.error("Menu item not found: {}", menuItemId);
                    return new ResourceNotFoundException("Menu item not found.");
                });
        AppUser user = userRepositoryPort.findByEmail(normalizeEmail(currentUserEmail))
                .orElseThrow(() -> {
                    logger.error("Authenticated user not found: {}", currentUserEmail);
                    return new ResourceNotFoundException("Authenticated user was not found.");
                });

        CustomerOrder currentOrder = customerOrderRepositoryPort.findOpenByTableId(tableId)
                .orElseGet(() -> createOpenOrder(tableId, user.id()));

        List<OrderLine> mergedLines = orderCalculator.mergeOrderLine(currentOrder.lines(), menuItem, quantity);
        CustomerOrder recalculatedOrder = orderCalculator.recalculate(currentOrder, mergedLines, OrderStatus.OPEN);
        customerOrderRepositoryPort.save(recalculatedOrder);

        if (table.status() == TableStatus.AVAILABLE || table.status() == TableStatus.CLEANING) {
            restaurantTableRepositoryPort.save(new RestaurantTable(table.id(), table.tableNumber(), TableStatus.OCCUPIED));
            logger.debug("Table {} status changed to OCCUPIED", tableId);
        }

        logger.info("Order line added successfully for table {}", tableId);
        return getDashboard(tableId);
    }

    @Override
    public TableDashboardView updateOrderLine(Long tableId, Long lineId, int quantity) {
        logger.info("Updating order line: tableId={}, lineId={}, new quantity={}", tableId, lineId, quantity);
        CustomerOrder currentOrder = getOpenOrder(tableId);
        ensureLineExists(currentOrder.lines(), lineId);
        List<OrderLine> updatedLines = orderCalculator.updateQuantity(currentOrder.lines(), lineId, quantity);
        customerOrderRepositoryPort.save(orderCalculator.recalculate(currentOrder, updatedLines, OrderStatus.OPEN));
        logger.info("Order line {} updated for table {}", lineId, tableId);
        return getDashboard(tableId);
    }

    @Override
    public TableDashboardView removeOrderLine(Long tableId, Long lineId) {
        logger.info("Removing order line: tableId={}, lineId={}", tableId, lineId);
        RestaurantTable table = getTable(tableId);
        CustomerOrder currentOrder = getOpenOrder(tableId);

        List<OrderLine> remainingLines = currentOrder.lines().stream()
                .filter(line -> !line.id().equals(lineId))
                .toList();

        if (remainingLines.size() == currentOrder.lines().size()) {
            logger.warn("Order line {} not found in table {}", lineId, tableId);
            throw new ResourceNotFoundException("Order line was not found.");
        }

        if (remainingLines.isEmpty()) {
            logger.debug("No remaining lines, deleting order and freeing table {}", tableId);
            customerOrderRepositoryPort.deleteById(currentOrder.id());
            restaurantTableRepositoryPort.save(new RestaurantTable(table.id(), table.tableNumber(), TableStatus.AVAILABLE));
            return getDashboard(tableId);
        }

        customerOrderRepositoryPort.save(orderCalculator.recalculate(currentOrder, remainingLines, OrderStatus.OPEN));
        logger.info("Order line {} removed from table {}", lineId, tableId);
        return getDashboard(tableId);
    }

    @Override
    public TableDashboardView closeOrder(Long tableId) {
        logger.info("Closing order for table {}", tableId);
        RestaurantTable table = getTable(tableId);
        CustomerOrder currentOrder = getOpenOrder(tableId);

        if (currentOrder.lines().isEmpty()) {
            logger.warn("Attempted to close an empty order for table {}", tableId);
            throw new BusinessRuleException("Cannot close an empty order.");
        }

        CustomerOrder closedOrder = orderCalculator.recalculate(currentOrder, currentOrder.lines(), OrderStatus.CLOSED);
        customerOrderRepositoryPort.save(closedOrder);
        restaurantTableRepositoryPort.save(new RestaurantTable(table.id(), table.tableNumber(), TableStatus.AVAILABLE));
        logger.info("Order closed for table {}", tableId);
        return getDashboard(tableId);
    }

    private RestaurantTable getTable(Long tableId) {
        return restaurantTableRepositoryPort.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found."));
    }

    private CustomerOrder getOpenOrder(Long tableId) {
        return customerOrderRepositoryPort.findOpenByTableId(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Open order was not found for this table."));
    }

    private CustomerOrder createOpenOrder(Long tableId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return new CustomerOrder(
                null,
                tableId,
                userId,
                OrderStatus.OPEN,
                BigDecimal.ZERO,
                now,
                now,
                List.of()
        );
    }

    private void ensureLineExists(List<OrderLine> lines, Long lineId) {
        boolean exists = lines.stream().anyMatch(line -> line.id().equals(lineId));
        if (!exists) {
            throw new ResourceNotFoundException("Order line was not found.");
        }
    }

    private List<MenuItem> getSortedMenu() {
        return menuItemRepositoryPort.findAllActive().stream()
                .sorted(Comparator
                        .comparingInt((MenuItem item) -> item.category().getDisplayOrder())
                        .thenComparing(MenuItem::name))
                .toList();
    }

    private TableSummaryView toSummary(RestaurantTable table, CustomerOrder currentOrder) {
        BigDecimal total = currentOrder == null ? BigDecimal.ZERO : currentOrder.total();
        int itemCount = currentOrder == null ? 0 : currentOrder.lines().stream()
                .mapToInt(OrderLine::quantity)
                .sum();

        return new TableSummaryView(
                table.id(),
                table.tableNumber(),
                table.status(),
                total,
                itemCount
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
