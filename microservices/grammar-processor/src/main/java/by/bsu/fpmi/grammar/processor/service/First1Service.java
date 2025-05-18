package by.bsu.fpmi.grammar.processor.service;

import by.bsu.fpmi.grammar.processor.model.Grammar;
import by.bsu.fpmi.grammar.processor.model.Symbol;
import by.bsu.fpmi.grammar.processor.model.Word;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@UtilityClass
public class First1Service {

    public static Set<Symbol> first1OverWord(Word word, Map<Symbol, Set<Symbol>> first1Set, Set<Symbol> nonTerminals) {

        Set<Symbol> result = new HashSet<>();
        updateNonTerminalFirst1Set(word, nonTerminals, first1Set, result);
        return result;
    }


    private static void updateNonTerminalFirst1Set(Word productionBody, Set<Symbol> nonTerminals, Map<Symbol, Set<Symbol>> previousFirst1Set, Set<Symbol> nonTerminalFirst1SetToUpdate) {

        int productionBodySize = productionBody.size();
        boolean nonTerminalSetContainsEmptySymbol = nonTerminalFirst1SetToUpdate.contains(Symbol.EMPTY_SYMBOL);

        for (int i = 0; i < productionBodySize; i++) {

            Symbol symbol = productionBody.getAt(i);

            if (!nonTerminals.contains(symbol)) {
                nonTerminalFirst1SetToUpdate.add(symbol);
                return;
            }

            Set<Symbol> productionNonTerminalFirst1 = previousFirst1Set.get(symbol);

            nonTerminalFirst1SetToUpdate.addAll(productionNonTerminalFirst1);

            if (!productionNonTerminalFirst1.contains(Symbol.EMPTY_SYMBOL)) {
                return;
            }

            if (!nonTerminalSetContainsEmptySymbol && i != productionBodySize - 1) {

                nonTerminalFirst1SetToUpdate.remove(Symbol.EMPTY_SYMBOL);
            }
        }
    }

    public static Map<Symbol, Set<Symbol>> first1(Grammar grammar) {

        Set<Symbol> nonTerminals = grammar.getNonTerminals();
        Map<Symbol, Set<Word>> definingEquations = grammar.getDefiningEquations();

        Map<Symbol, Set<Symbol>> currentFirst1Set = new HashMap<>();
        Map<Symbol, Set<Symbol>> previousFirst1Set = new HashMap<>();

        for (Symbol nonTerminal : nonTerminals) {
            currentFirst1Set.put(nonTerminal, new HashSet<>());
            previousFirst1Set.put(nonTerminal, new HashSet<>());
        }

        do {
            nonTerminals.forEach(nonTerminal ->
                    previousFirst1Set.get(nonTerminal).addAll(currentFirst1Set.get(nonTerminal)));

            nonTerminals.forEach(nonTerminal ->
                    iterateOnNonTerminal(nonTerminal, nonTerminals, definingEquations.get(nonTerminal), currentFirst1Set, previousFirst1Set));

            log.debug("PREV {}", previousFirst1Set);
            log.debug("CURRENT: {}", currentFirst1Set);

        } while (!previousFirst1Set.equals(currentFirst1Set));

        return currentFirst1Set;
    }

    private static void iterateOnNonTerminal(
            Symbol nonTerminal,
            Set<Symbol> nonTerminals,
            Set<Word> defEquationProductionBodies,
            Map<Symbol, Set<Symbol>> currentFirst1Set,
            Map<Symbol, Set<Symbol>> previousFirst1Set
    ) {
        for (Word productionBody : defEquationProductionBodies) {

            updateNonTerminalFirst1Set(productionBody, nonTerminals, previousFirst1Set, currentFirst1Set.get(nonTerminal));
        }
    }
}
