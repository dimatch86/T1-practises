package com.t1.jwt_auth_app.exception;

import com.t1.jwt_auth_app.model.response.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(formErrorResponse(errorMessage));
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(formErrorResponse(e.getLocalizedMessage()));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
             MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleDuplicateKeyException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(formErrorResponse(e.getLocalizedMessage()));
    }

    @ExceptionHandler({SQLException.class, RoleException.class, SelfModificationException.class})
    public ResponseEntity<ErrorResponse> handleModificationException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                .body(formErrorResponse(e.getMessage()));
    }

    private ErrorResponse formErrorResponse(String errorMessage) {
        return ErrorResponse.builder()
                .error(errorMessage)
                .build();
    }
}
