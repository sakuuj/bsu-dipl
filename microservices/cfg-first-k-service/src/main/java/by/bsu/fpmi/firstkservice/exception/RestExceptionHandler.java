package by.bsu.fpmi.firstkservice.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getPropertyName(), ex.getValue(), ex.getRequiredType());

        ApiError apiError = new ApiError();
        apiError.setMessage(message);
        apiError.setStatus(status.toString());
        apiError.setDebugMessage(ex.getMessage());
        apiError.setTimestamp(Instant.now());

        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setMessage("Incorrect JSON request");
        apiError.setStatus(status.toString());
        apiError.setDebugMessage(ex.getMessage());
        apiError.setTimestamp(Instant.now());

        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException exception,
                                                                  WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError apiError = new ApiError();
        apiError.setMessage("Illegal argument");
        apiError.setDebugMessage(exception.getMessage());
        apiError.setStatus(status.toString());
        apiError.setTimestamp(Instant.now());

        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ApiError apiError = new ApiError();
        apiError.setMessage("Validation failed");
        apiError.setErrors(errors);
        apiError.setStatus(status.toString());
        apiError.setDebugMessage(ex.getMessage());
        apiError.setTimestamp(Instant.now());

        return new ResponseEntity<>(apiError, status);
    }
}
