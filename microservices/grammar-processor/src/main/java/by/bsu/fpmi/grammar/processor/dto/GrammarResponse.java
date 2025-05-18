package by.bsu.fpmi.grammar.processor.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GrammarResponse(
        List<String> nonTerminals,
        List<String> terminals,
        String startSymbol,
        List<String> definingEquations
) {
}
