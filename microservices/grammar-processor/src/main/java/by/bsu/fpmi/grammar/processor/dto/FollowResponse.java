package by.bsu.fpmi.grammar.processor.dto;

import lombok.Builder;

import java.util.Map;
import java.util.Set;

@Builder
public record FollowResponse(
        GrammarResponse grammarResponse,
        Map<String, Set<String>> first1Map,
        Map<String, Set<String>> followMap
) {
}
