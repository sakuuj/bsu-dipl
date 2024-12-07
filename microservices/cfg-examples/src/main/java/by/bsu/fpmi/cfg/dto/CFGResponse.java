package by.bsu.fpmi.cfg.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CFGResponse(
        String id,
        List<String> nonTerminals,
        List<String> terminals,
        List<String> definingEquations,
        String startSymbol
) {
}
