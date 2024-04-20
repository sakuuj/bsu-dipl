package by.bsu.fpmi.processor.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception exception) {

        ApiError apiError = ApiError.builder()
                .timestamp(Instant.now())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.internalServerError()
                .body(apiError);
    }

    @ExceptionHandler(MalformedGrammarException.class)
    public ResponseEntity<ApiError> handleMalformedGrammarException(MalformedGrammarException exception) {

        ApiError apiError = ApiError.builder()
                .timestamp(Instant.now())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.badRequest()
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
