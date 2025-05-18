package by.bsu.fpmi.grammar.examples.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GrammarResponse(
        String id,
        List<String> nonTerminals,
        List<String> terminals,
        List<String> definingEquations,
        String startSymbol
) {
}
