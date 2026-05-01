package com.foodtrack.spring.springboot_application.config;

import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null, Collections.emptyMap());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage(),
                        (current, ignored) -> current,
                        LinkedHashMap::new
                ));
        String field = errors.keySet().stream().findFirst().orElse(null);
        String message = field == null ? "Validation error" : field + " " + errors.get(field);
        return buildResponse(HttpStatus.BAD_REQUEST, message, field, errors);
    }

    @ExceptionHandler({BusinessRuleException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), null, Collections.emptyMap());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException exception) {
        return buildResponse(HttpStatus.FORBIDDEN, "You do not have permission to perform this action.", null, Collections.emptyMap());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedError(Exception exception) {
        logger.error("Unexpected server error", exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error.", null, Collections.emptyMap());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            String field,
            Map<String, String> errors
    ) {
        ApiErrorResponse body = new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                field,
                errors
        );
        return ResponseEntity.status(status).body(body);
    }
}
