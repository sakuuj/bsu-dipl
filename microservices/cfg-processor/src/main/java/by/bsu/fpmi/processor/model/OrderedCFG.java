package by.bsu.fpmi.processor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class OrderedCFG {
    private Map<Symbol, Set<Word>> definingEquations;
    private LinkedHashSet<Symbol> nonTerminals;
    private LinkedHashSet<Symbol> terminals;
    private Symbol startSymbol;
}
