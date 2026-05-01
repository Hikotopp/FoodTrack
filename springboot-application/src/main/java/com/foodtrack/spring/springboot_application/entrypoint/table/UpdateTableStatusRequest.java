package com.foodtrack.spring.springboot_application.entrypoint.table;

import com.foodtrack.spring.springboot_application.domain.model.TableStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTableStatusRequest(
        @NotNull TableStatus status
) {
}
