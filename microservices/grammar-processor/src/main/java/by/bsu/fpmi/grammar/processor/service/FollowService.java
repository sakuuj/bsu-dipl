package by.bsu.fpmi.grammar.processor.service;

import by.bsu.fpmi.grammar.processor.model.Grammar;
import by.bsu.fpmi.grammar.processor.model.Symbol;
import by.bsu.fpmi.grammar.processor.model.Word;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class FollowService {

    public static Map<Symbol, Set<Symbol>> follow(Grammar grammar, Map<Symbol, Set<Symbol>> first1) {

        Set<Symbol> nonTerminals = grammar.getNonTerminals();

        Map<Symbol, Set<Word>> definingEquations = grammar.getDefiningEquations();

        Symbol startSymbol = grammar.getStartSymbol();

        Map<Symbol, Set<Symbol>> currentFollowSet = new HashMap<>();
        Map<Symbol, Set<Symbol>> previousFollowSet = new HashMap<>();
        nonTerminals.forEach(nonTerminal -> {
            previousFollowSet.put(nonTerminal, new HashSet<>());
            currentFollowSet.put(nonTerminal, new HashSet<>());
        });
        previousFollowSet.get(startSymbol).add(Symbol.RESERVED_SYMBOL);
        currentFollowSet.get(startSymbol).add(Symbol.RESERVED_SYMBOL);

        do {
            nonTerminals.forEach(nonTerminal ->
                    previousFollowSet.get(nonTerminal).addAll(currentFollowSet.get(nonTerminal))
            );

            for (Symbol nonTerminal : nonTerminals) {

                Set<Symbol> nonTerminalPreviousFollowSet = previousFollowSet.get(nonTerminal);
                Set<Word> productionBodies = definingEquations.get(nonTerminal);
                for (Word productionBody : productionBodies) {

                    int size = productionBody.size();

                    for (int i = 0; i < size - 1; i++) {

                        Symbol symbol = productionBody.getAt(i);

                        if (!nonTerminals.contains(symbol)) {
                            continue;
                        }

                        Word suffix = productionBody.subWord(i + 1);
                        Set<Symbol> suffixFirst1Set = First1Service.first1OverWord(suffix, first1, nonTerminals);
                        suffixFirst1Set.remove(Symbol.EMPTY_SYMBOL);

                        currentFollowSet.get(symbol).addAll(suffixFirst1Set);
                    }

                    boolean suffixFirstSetContainsEmptySymbol = true;
                    for (int i = size - 1; i >= 0 && suffixFirstSetContainsEmptySymbol; i--) {
                        Symbol symbol = productionBody.getAt(i);
                        if (!nonTerminals.contains(symbol)) {
                            break;
                        }
                        currentFollowSet.get(symbol).addAll(nonTerminalPreviousFollowSet);

                        if (!first1.get(symbol).contains(Symbol.EMPTY_SYMBOL)) {
                            suffixFirstSetContainsEmptySymbol = false;
                        }
                    }
                }
            }

        } while (!currentFollowSet.equals(previousFollowSet));

        return currentFollowSet;
    }
}
