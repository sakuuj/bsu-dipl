package by.bsu.fpmi.grammar.processor.dto;

import by.bsu.fpmi.grammar.processor.enums.ParsingStatus;
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
