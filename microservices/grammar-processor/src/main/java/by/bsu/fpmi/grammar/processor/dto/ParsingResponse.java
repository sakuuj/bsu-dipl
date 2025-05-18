package by.bsu.fpmi.grammar.processor.dto;

import lombok.Builder;

@Builder
public record ParsingResponse(
        GrammarResponse grammarResponse,
        ParsingMetadata parsingMetadata
) {
}
