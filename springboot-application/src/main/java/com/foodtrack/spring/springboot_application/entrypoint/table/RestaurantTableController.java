package com.foodtrack.spring.springboot_application.entrypoint.table;

import com.foodtrack.spring.springboot_application.application.model.TableDashboardView;
import com.foodtrack.spring.springboot_application.application.model.TableSummaryView;
import com.foodtrack.spring.springboot_application.application.port.in.TableUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tables")
@Tag(name = "Tables")
@SecurityRequirement(name = "bearerAuth")
public class RestaurantTableController {

    private final TableUseCase tableUseCase;
    private final TableMapper tableMapper;

    public RestaurantTableController(TableUseCase tableUseCase, TableMapper tableMapper) {
        this.tableUseCase = tableUseCase;
        this.tableMapper = tableMapper;
    }

    @GetMapping
    @Operation(summary = "List all restaurant tables with totals and item counts")
    public ResponseEntity<List<TableSummaryResponse>> listTables() {
        List<TableSummaryView> views = tableUseCase.listTables();
        List<TableSummaryResponse> responses = views.stream()
                .map(tableMapper::toSummaryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{tableId}")
    @Operation(summary = "Get the dashboard for a single table")
    public ResponseEntity<TableDashboardResponse> getDashboard(@PathVariable Long tableId) {
        TableDashboardView view = tableUseCase.getDashboard(tableId);
        return ResponseEntity.ok(tableMapper.toDashboardResponse(view));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new restaurant table")
    public ResponseEntity<TableSummaryResponse> createTable(@Valid @RequestBody CreateTableRequest request) {
        TableSummaryView created = tableUseCase.createTable(request.tableNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(tableMapper.toSummaryResponse(created));
    }

    @DeleteMapping("/{tableId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a table without active orders")
    public ResponseEntity<Void> deleteTable(@PathVariable Long tableId) {
        tableUseCase.deleteTable(tableId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tableId}/status")
    @Operation(summary = "Update the operational status of a table")
    public ResponseEntity<TableSummaryResponse> updateTableStatus(
            @PathVariable Long tableId,
            @Valid @RequestBody UpdateTableStatusRequest request
    ) {
        TableSummaryView updated = tableUseCase.updateTableStatus(tableId, request.status());
        return ResponseEntity.ok(tableMapper.toSummaryResponse(updated));
    }

    @PostMapping("/{tableId}/order-lines")
    @Operation(summary = "Add a menu item to the current table order")
    public ResponseEntity<TableDashboardResponse> addOrderLine(
            @PathVariable Long tableId,
            @Valid @RequestBody AddOrderLineRequest request,
            Authentication authentication
    ) {
        TableDashboardView view = tableUseCase.addOrderLine(tableId, request.menuItemId(), request.quantity(), authentication.getName());
        return ResponseEntity.ok(tableMapper.toDashboardResponse(view));
    }

    @PatchMapping("/{tableId}/order-lines/{lineId}")
    @Operation(summary = "Update the quantity of an order line")
    public ResponseEntity<TableDashboardResponse> updateOrderLine(
            @PathVariable Long tableId,
            @PathVariable Long lineId,
            @Valid @RequestBody UpdateOrderLineRequest request
    ) {
        TableDashboardView view = tableUseCase.updateOrderLine(tableId, lineId, request.quantity());
        return ResponseEntity.ok(tableMapper.toDashboardResponse(view));
    }

    @DeleteMapping("/{tableId}/order-lines/{lineId}")
    @Operation(summary = "Remove an order line from the table order")
    public ResponseEntity<TableDashboardResponse> removeOrderLine(
            @PathVariable Long tableId,
            @PathVariable Long lineId
    ) {
        TableDashboardView view = tableUseCase.removeOrderLine(tableId, lineId);
        return ResponseEntity.ok(tableMapper.toDashboardResponse(view));
    }

    @PostMapping("/{tableId}/close-order")
    @Operation(summary = "Close the current order and free the table")
    public ResponseEntity<TableDashboardResponse> closeOrder(@PathVariable Long tableId) {
        TableDashboardView view = tableUseCase.closeOrder(tableId);
        return ResponseEntity.ok(tableMapper.toDashboardResponse(view));
    }
}