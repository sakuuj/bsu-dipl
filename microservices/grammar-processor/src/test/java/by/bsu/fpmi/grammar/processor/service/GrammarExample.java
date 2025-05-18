package by.bsu.fpmi.grammar.processor.service;

import by.bsu.fpmi.grammar.processor.model.Grammar;
import by.bsu.fpmi.grammar.processor.model.Symbol;
import lombok.Builder;

import java.util.Map;
import java.util.Set;

@Builder
public record GrammarExample(
        Grammar grammar,
        Map<Symbol, Set<Symbol>> first1Set,
        Map<Symbol, Set<Symbol>> followSet
) {
}
