package by.bsu.fpmi.firstkservice.model;

import lombok.*;

import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class ContextFreeGrammar {
    private Map<Character, Set<String>> nonTerminalsToTransformationOptions;
    private Set<Character> terminals;
    private Character startSymbol;
}
