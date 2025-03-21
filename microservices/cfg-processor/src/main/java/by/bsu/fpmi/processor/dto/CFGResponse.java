package by.bsu.fpmi.processor.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CFGResponse(
        List<String> nonTerminals,
        List<String> terminals,
        String startSymbol,
        List<String> definingEquations
) {
}
