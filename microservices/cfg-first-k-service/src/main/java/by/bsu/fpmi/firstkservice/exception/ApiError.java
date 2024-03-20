package by.bsu.fpmi.firstkservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ApiError {
    private String message;
    private String status;
    private String debugMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    private Instant timestamp;

}
