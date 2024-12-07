package by.bsu.fpmi.processor.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
public record FollowResponse(
        CFGResponse cfgResponse,
        Map<String, Set<String>> first1Map,
        Map<String, Set<String>> followMap
) {
}
