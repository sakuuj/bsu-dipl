package by.bsu.fpmi.grammar.examples.controller;

import by.bsu.fpmi.grammar.examples.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception exception) {

        log.error("Error", exception);

        ApiError apiError = ApiError.builder()
                .timestamp(Instant.now())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.internalServerError()
                .body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(AuthenticationException ex) {

        log.info("Unauthorized: {}", ex.getMessage());

        ApiError apiError = ApiError.builder()
                .message("Authentication failure")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ApiError apiError = ApiError.builder()
                .message("Validation failed")
                .errors(errors)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.badRequest()
                .body(apiError);
    }
}
