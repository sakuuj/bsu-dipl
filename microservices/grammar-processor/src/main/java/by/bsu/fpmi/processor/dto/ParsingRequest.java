package by.bsu.fpmi.processor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ParsingRequest(
        @NotNull
        GrammarRequest grammarRequest,
        @NotNull
        String text
) {
}
