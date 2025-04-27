package by.bsu.fpmi.processor.dto;

import lombok.Builder;

@Builder
public record ParsingResponse(
        GrammarResponse grammarResponse,
        ParsingMetadata parsingMetadata
) {
}
