package by.bsu.fpmi.firstkservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class OrderedCFG {
    private Map<Symbol, Set<Word>> definingEquations;
    private List<Symbol> nonTerminals;
    private List<Symbol> terminals;
    private Symbol startSymbol;
}
