package by.bsu.fpmi.processor.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ApiError {

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    private Instant timestamp;

}
