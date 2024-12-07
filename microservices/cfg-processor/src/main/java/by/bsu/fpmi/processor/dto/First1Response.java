package by.bsu.fpmi.processor.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
public record First1Response(
        CFGResponse cfgResponse,
        Map<String, Set<String>> first1Map
) {
}