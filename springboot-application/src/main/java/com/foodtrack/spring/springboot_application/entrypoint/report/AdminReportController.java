package com.foodtrack.spring.springboot_application.entrypoint.report;

import com.foodtrack.spring.springboot_application.application.model.SalesHistoryView;
import com.foodtrack.spring.springboot_application.application.port.in.SalesHistoryUseCase;
import com.foodtrack.spring.springboot_application.domain.model.OrderLine;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/reports")
@Tag(name = "Admin Reports")
@SecurityRequirement(name = "bearerAuth")
public class AdminReportController {

    private final SalesHistoryUseCase salesHistoryUseCase;

    public AdminReportController(SalesHistoryUseCase salesHistoryUseCase) {
        this.salesHistoryUseCase = salesHistoryUseCase;
    }

    @PostMapping("/generate-now")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generate sales report summary")
    public ResponseEntity<AdminReportResponse> generateNow() {
        List<SalesHistoryView> sales = salesHistoryUseCase.listHistory();
        BigDecimal totalClosedSales = sales.stream()
                .filter(sale -> sale.status() == OrderStatus.CLOSED)
                .map(SalesHistoryView::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long closedSalesCount = sales.stream()
                .filter(sale -> sale.status() == OrderStatus.CLOSED)
                .count();
        long cancelledSalesCount = sales.stream()
                .filter(sale -> sale.status() == OrderStatus.CANCELLED)
                .count();
        List<TopItemResponse> topItems = sales.stream()
                .filter(sale -> sale.status() == OrderStatus.CLOSED)
                .flatMap(sale -> sale.lines().stream())
                .collect(Collectors.groupingBy(
                        OrderLine::itemName,
                        Collectors.summingInt(OrderLine::quantity)
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> new TopItemResponse(entry.getKey(), entry.getValue()))
                .toList();

        ReportSummaryResponse summary = new ReportSummaryResponse(
                sales.size(),
                closedSalesCount,
                cancelledSalesCount,
                totalClosedSales,
                topItems
        );

        return ResponseEntity.ok(new AdminReportResponse(
                true,
                "Reporte generado correctamente.",
                summary,
                new ReportServiceResponse(
                        true,
                        false,
                        List.of(),
                        0,
                        "El envio por correo no esta configurado."
                )
        ));
    }
}
