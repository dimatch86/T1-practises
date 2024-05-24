package com.t1.loggableapplication.exception;

import com.t1.loggableapplication.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getLocalizedMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MissedParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissedParameterException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getLocalizedMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
