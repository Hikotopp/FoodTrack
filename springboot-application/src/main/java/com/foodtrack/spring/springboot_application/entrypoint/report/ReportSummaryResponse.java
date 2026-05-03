package com.foodtrack.spring.springboot_application.entrypoint.report;

import java.math.BigDecimal;
import java.util.List;

public record ReportSummaryResponse(
        int salesCount,
        long closedSalesCount,
        long cancelledSalesCount,
        BigDecimal totalClosedSales,
        List<TopItemResponse> topItems
) {
}
