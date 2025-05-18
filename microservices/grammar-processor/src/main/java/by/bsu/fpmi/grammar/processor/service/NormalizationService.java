package by.bsu.fpmi.grammar.processor.service;

import by.bsu.fpmi.grammar.processor.model.Grammar;
import by.bsu.fpmi.grammar.processor.model.Symbol;
import by.bsu.fpmi.grammar.processor.model.Word;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.Map.Entry;

@UtilityClass
public class NormalizationService {

    public static void removeUselessNonTerminals(Grammar grammar) {
        retainGeneratingNonTerminals(grammar);
        retainReachableNonTerminals(grammar);
    }

    public static void retainGeneratingNonTerminals(Grammar grammar) {

        Set<Symbol> terminals = grammar.getTerminals();
        Map<Symbol, Set<Word>> defEquationsMap = grammar.getDefiningEquations();

        Set<Symbol> generatingNonTerminals = new HashSet<>();

        Set<Entry<Symbol, Set<Word>>> defEquations = defEquationsMap.entrySet();

        int generatingCount = -1;
        while (generatingCount != generatingNonTerminals.size()) {

            generatingCount = generatingNonTerminals.size();

            for (Entry<Symbol, Set<Word>> symbolToDefEquation : defEquations) {

                Symbol nonTerminal = symbolToDefEquation.getKey();
                if (generatingNonTerminals.contains(nonTerminal)) {
                    continue;
                }

                Set<Word> defEquation = symbolToDefEquation.getValue();

                for (Word option : defEquation) {

                    boolean allSymbolsAreGenerating = true;

                    for (Symbol symbol : option) {

                        if (!generatingNonTerminals.contains(symbol) && !terminals.contains(symbol) && symbol != Symbol.EMPTY_SYMBOL) {
                            allSymbolsAreGenerating = false;
                            break;
                        }
                    }

                    if (allSymbolsAreGenerating) {
                        generatingNonTerminals.add(nonTerminal);
                        break;
                    }
                }
            }
        }

        Map<Symbol, Set<Word>> changedDefEquations = new HashMap<>();
        for (Symbol generatingNonTerminal : generatingNonTerminals) {

            Set<Word> changedOptions = new HashSet<>(defEquationsMap.get(generatingNonTerminal));
            Set<Word> optionsToRemove = new HashSet<>();

            for (Word word : changedOptions) {

                for (Symbol symbol : word) {

                    if (!generatingNonTerminals.contains(symbol) && !terminals.contains(symbol) && symbol != Symbol.EMPTY_SYMBOL) {
                        optionsToRemove.add(word);
                        break;
                    }
                }
            }

            changedOptions.removeAll(optionsToRemove);
            changedDefEquations.put(generatingNonTerminal, changedOptions);
        }

        grammar.setDefiningEquations(changedDefEquations);

        var grammarNonTerminals = grammar.getNonTerminals();
        grammarNonTerminals.retainAll(generatingNonTerminals);

        if (!grammarNonTerminals.contains(grammar.getStartSymbol())) {
            grammar.setStartSymbol(null);
        }
    }

    public static void retainReachableNonTerminals(Grammar grammar) {

        Map<Symbol, Set<Word>> defEquationsMap = grammar.getDefiningEquations();
        Symbol startSymbol = grammar.getStartSymbol();

        if (!defEquationsMap.containsKey(startSymbol)) {
            grammar.setNonTerminals(new LinkedHashSet<>());
            grammar.setDefiningEquations(Map.of());
            grammar.setStartSymbol(null);
            return;
        }

        Set<Symbol> nonTerminals = grammar.getNonTerminals();

        Set<Symbol> reachableNonTerminals = new HashSet<>(Set.of(startSymbol));

        int reachableCount = -1;
        while (reachableCount != reachableNonTerminals.size()) {

            reachableCount = reachableNonTerminals.size();

            Set<Symbol> nonTerminalsToAddToReachable = new HashSet<>();

            for (Symbol reachableNonTerminal : reachableNonTerminals) {

                Set<Word> nonTerminalEquation = defEquationsMap.get(reachableNonTerminal);

                for (Word option : nonTerminalEquation) {

                    for (Symbol symbol : option) {

                        if (nonTerminals.contains(symbol)) {

                            nonTerminalsToAddToReachable.add(symbol);
                        }
                    }
                }
            }

            reachableNonTerminals.addAll(nonTerminalsToAddToReachable);
        }


        Map<Symbol, Set<Word>> reachableDefEquations = new HashMap<>();
        for (Symbol reachableNonTerminal : reachableNonTerminals) {

            Set<Word> reachableOptions = new HashSet<>(defEquationsMap.get(reachableNonTerminal));
            reachableDefEquations.put(reachableNonTerminal, reachableOptions);
        }

        grammar.getNonTerminals().retainAll(reachableNonTerminals);
        grammar.setDefiningEquations(reachableDefEquations);
    }
}
