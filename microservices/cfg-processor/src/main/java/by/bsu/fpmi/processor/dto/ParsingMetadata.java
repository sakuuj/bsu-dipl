package by.bsu.fpmi.processor.dto;

import by.bsu.fpmi.processor.enums.ParsingStatus;
import by.bsu.fpmi.processor.model.Symbol;
import lombok.Builder;

import java.util.List;

@Builder
public record ParsingMetadata(
        ParsingStatus parsingStatus,
        List<ParsingStep> parsingSteps
) {
    @Builder
    public record ParsingStep(
            String actionDescription,
            String stackContent,
            String unmatchedInput
    ) {
    }
}
