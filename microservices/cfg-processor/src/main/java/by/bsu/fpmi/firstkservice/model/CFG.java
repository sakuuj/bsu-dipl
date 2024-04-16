package by.bsu.fpmi.firstkservice.model;

import lombok.*;

import java.util.List;
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
