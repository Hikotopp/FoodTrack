package com.foodtrack.spring.springboot_application.config;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String field,
        Map<String, String> errors
) {
}
