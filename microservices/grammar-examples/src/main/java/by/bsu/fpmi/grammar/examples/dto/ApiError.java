package by.bsu.fpmi.grammar.examples.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;
import java.util.List;


@Builder
public record ApiError(
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> errors,
        Instant timestamp
) {
}
