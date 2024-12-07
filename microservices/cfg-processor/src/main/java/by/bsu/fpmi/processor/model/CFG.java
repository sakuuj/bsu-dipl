package by.bsu.fpmi.processor.model;

import lombok.*;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CFG {
    private Map<Symbol, Set<Word>> definingEquations;
    private LinkedHashSet<Symbol> nonTerminals;
    private LinkedHashSet<Symbol> terminals;
    private Symbol startSymbol;
}
