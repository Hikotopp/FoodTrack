package com.foodtrack.spring.springboot_application.entrypoint.report;

public record AdminReportResponse(
        boolean success,
        String message,
        ReportSummaryResponse summary,
        ReportServiceResponse reportService
) {
}
