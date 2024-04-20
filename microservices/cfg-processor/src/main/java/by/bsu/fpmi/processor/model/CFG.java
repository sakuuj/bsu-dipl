package by.bsu.fpmi.processor.model;

import lombok.*;

import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class CFG {
    private Map<Symbol, Set<Word>> definingEquations;
    private Set<Symbol> nonTerminals;
    private Set<Symbol> terminals;
    private Symbol startSymbol;
}
