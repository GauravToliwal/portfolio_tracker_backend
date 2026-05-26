package com.wealthtracker.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, details);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, List.of(exception.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException exception) {
        return buildResponse(HttpStatus.UNAUTHORIZED, List.of(exception.getMessage()));
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ResourceConflictException exception) {
        return buildResponse(HttpStatus.CONFLICT, List.of(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of(exception.getMessage()));
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, List<String> details) {
        return ResponseEntity.status(status).body(
            new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), details)
        );
    }
}
