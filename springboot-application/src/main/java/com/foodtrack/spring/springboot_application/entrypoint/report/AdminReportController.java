package com.foodtrack.spring.springboot_application.entrypoint.report;

import com.foodtrack.spring.springboot_application.application.model.SalesHistoryView;
import com.foodtrack.spring.springboot_application.application.port.in.SalesHistoryUseCase;
import com.foodtrack.spring.springboot_application.domain.model.OrderLine;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/reports")
@Tag(name = "Admin Reports")
@SecurityRequirement(name = "bearerAuth")
public class AdminReportController {

    private final SalesHistoryUseCase salesHistoryUseCase;
    private final RestClient reportServiceClient;

    public AdminReportController(
            SalesHistoryUseCase salesHistoryUseCase,
            RestClient.Builder restClientBuilder,
            @Value("${report-service.base-url:http://localhost:8081}") String reportServiceBaseUrl
    ) {
        this.salesHistoryUseCase = salesHistoryUseCase;
        this.reportServiceClient = restClientBuilder
                .baseUrl(Objects.requireNonNull(reportServiceBaseUrl, "reportServiceBaseUrl"))
                .build();
    }

    @PostMapping("/generate-now")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generate and send sales report")
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

        ReportServiceResponse reportServiceResponse = sendReportThroughReportService();

        return ResponseEntity.ok(new AdminReportResponse(
                true,
                reportServiceResponse.emailSent()
                        ? "Reporte generado y enviado correctamente."
                        : "Reporte generado correctamente.",
                summary,
                reportServiceResponse
        ));
    }

    private ReportServiceResponse sendReportThroughReportService() {
        try {
            Map<String, Object> response = reportServiceClient.post()
                    .uri("/api/reports/generate-and-send")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (response == null) {
                return new ReportServiceResponse(false, false, List.of(), 0, "ReportService no devolvio respuesta.");
            }

            return new ReportServiceResponse(
                    asBoolean(response.get("success")),
                    asBoolean(response.get("emailSent")),
                    asStringList(response.get("sentTo")),
                    asInt(response.get("sentCount")),
                    asString(response.get("emailError"))
            );
        } catch (Exception exception) {
            return new ReportServiceResponse(
                    false,
                    false,
                    List.of(),
                    0,
                    "No se pudo conectar con ReportService: " + exception.getMessage()
            );
        }
    }

    private boolean asBoolean(Object value) {
        return value instanceof Boolean booleanValue && booleanValue;
    }

    private int asInt(Object value) {
        return value instanceof Number numberValue ? numberValue.intValue() : 0;
    }

    private String asString(Object value) {
        return value == null ? null : value.toString();
    }

    private List<String> asStringList(Object value) {
        if (value instanceof List<?> listValue) {
            return listValue.stream()
                    .map(Object::toString)
                    .toList();
        }
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return List.of(stringValue);
        }
        return Collections.emptyList();
    }
}
