package com.foodtrack.spring.springboot_application.entrypoint.report;

public record TopItemResponse(
        String itemName,
        int quantity
) {
}
