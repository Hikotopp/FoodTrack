package com.foodtrack.spring.springboot_application.entrypoint.sales;

import com.foodtrack.spring.springboot_application.application.port.in.SalesHistoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/history")
@Tag(name = "Sales History")
@SecurityRequirement(name = "bearerAuth")
public class SalesHistoryController {

    private final SalesHistoryUseCase salesHistoryUseCase;
    private final SalesHistoryMapper salesHistoryMapper;

    public SalesHistoryController(SalesHistoryUseCase salesHistoryUseCase, SalesHistoryMapper salesHistoryMapper) {
        this.salesHistoryUseCase = salesHistoryUseCase;
        this.salesHistoryMapper = salesHistoryMapper;
    }

    @GetMapping
    @Operation(summary = "List closed and cancelled sales")
    public ResponseEntity<List<SalesHistoryResponse>> listHistory() {
        return ResponseEntity.ok(salesHistoryUseCase.listHistory().stream()
                .map(salesHistoryMapper::toResponse)
                .toList());
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a historical sale status")
    public ResponseEntity<SalesHistoryResponse> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateSaleStatusRequest request
    ) {
        return ResponseEntity.ok(salesHistoryMapper.toResponse(
                salesHistoryUseCase.updateSaleStatus(orderId, request.status())
        ));
    }
}
