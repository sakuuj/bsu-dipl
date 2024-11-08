package by.bsu.fpmi.processor.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
public record First1Response(
        List<String> nonTerminals,
        List<String> terminals,
        String startSymbol,
        List<String> definingEquations,
        Map<String, Set<String>> first1Map
) {
}