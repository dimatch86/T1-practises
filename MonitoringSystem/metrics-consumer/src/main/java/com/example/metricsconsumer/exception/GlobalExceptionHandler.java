package com.example.metricsconsumer.exception;

import com.example.metricsconsumer.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MetricNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMetricNotFoundException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getLocalizedMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
