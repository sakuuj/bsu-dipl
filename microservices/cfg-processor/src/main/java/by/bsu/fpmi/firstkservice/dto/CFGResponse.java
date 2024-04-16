package by.bsu.fpmi.firstkservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class CFGResponse {

    private List<String> nonTerminals;

    private List<String> terminals;

    private Map<String, Set<String>> nonTerminalToSolution;

    private String startSymbol;
}