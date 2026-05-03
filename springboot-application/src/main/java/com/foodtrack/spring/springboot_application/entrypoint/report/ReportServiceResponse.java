package com.foodtrack.spring.springboot_application.entrypoint.report;

import java.util.List;

public record ReportServiceResponse(
        boolean success,
        boolean emailSent,
        List<String> sentTo,
        int sentCount,
        String emailError
) {
}
