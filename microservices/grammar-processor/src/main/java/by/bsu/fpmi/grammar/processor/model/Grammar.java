package by.bsu.fpmi.grammar.processor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grammar {
    private Map<Symbol, Set<Word>> definingEquations;
    private LinkedHashSet<Symbol> nonTerminals;
    private LinkedHashSet<Symbol> terminals;
    private Symbol startSymbol;
}
