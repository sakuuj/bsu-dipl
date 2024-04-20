package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class NormalizingService {

    public static void retainGeneratingNonTerminals(CFG cfg) {
        Set<Symbol> terminals = cfg.getTerminals();
        Map<Symbol, Set<Word>> defEquationsMap = cfg.getDefiningEquations();

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

                    boolean hasGeneratingOption = true;

                    for (Symbol symbol : option) {

                        if (!generatingNonTerminals.contains(symbol) && !terminals.contains(symbol)) {
                            hasGeneratingOption = false;
                            break;
                        }
                    }

                    if (hasGeneratingOption) {
                        generatingNonTerminals.add(nonTerminal);
                        break;
                    }
                }
            }

        }

        Map<Symbol, Set<Word>> changedDefEquations = new HashMap<>();
        for (Symbol generatingNonTerminal : generatingNonTerminals) {

            Set<Word> changedOptions = defEquationsMap.get(generatingNonTerminal)
                    .stream()
                    .filter(w -> {
                        for (int i = 0; i < w.length(); i++) {
                            Symbol s = w.getAt(i);
                            if (!generatingNonTerminals.contains(s) && !terminals.contains(s)) {
                                return false;
                            }
                        }

                        return true;
                    })
                    .collect(Collectors.toCollection(HashSet::new));

            changedDefEquations.put(generatingNonTerminal, changedOptions);
        }

        cfg.setNonTerminals(generatingNonTerminals);
        cfg.setDefiningEquations(changedDefEquations);
    }

    public static void retainReachableNonTerminals(CFG cfg) {
        Map<Symbol, Set<Word>> defEquationsMap = cfg.getDefiningEquations();
        Symbol startSymbol = cfg.getStartSymbol();

        if (!defEquationsMap.containsKey(startSymbol)) {
            cfg.setNonTerminals(Set.of());
            cfg.setDefiningEquations(Map.of());
            cfg.setStartSymbol(null);
            return;
        }

        Set<Symbol> nonTerminals = cfg.getNonTerminals();

        Set<Symbol> reachableNonTerminals = new HashSet<>(Set.of(startSymbol));
        Set<Symbol> iteratedOverNonTerminals = new HashSet<>();

        int reachableCount = -1;
        while (reachableCount != reachableNonTerminals.size()) {

            reachableCount = reachableNonTerminals.size();

            for (Symbol reachableNonTerminal : reachableNonTerminals) {

                if (iteratedOverNonTerminals.contains(reachableNonTerminal)) {
                    continue;
                }
                iteratedOverNonTerminals.add(reachableNonTerminal);

                Set<Word> nonTerminalEquation = defEquationsMap.get(reachableNonTerminal);

                for (Word option : nonTerminalEquation) {

                    for (Symbol symbol : option) {

                        if (nonTerminals.contains(symbol)) {

                            reachableNonTerminals.add(symbol);
                        }
                    }
                }
            }

        }

        Set<Symbol> terminals = cfg.getTerminals();

        Map<Symbol, Set<Word>> changedDefEquations = new HashMap<>();
        for (Symbol reachableNonTerminal : reachableNonTerminals) {

            Set<Word> changedOptions = defEquationsMap.get(reachableNonTerminal)
                    .stream()
                    .filter(w -> {
                        for (int i = 0; i < w.length(); i++) {
                            Symbol s = w.getAt(i);
                            if (!reachableNonTerminals.contains(s) && !terminals.contains(s)) {
                                return false;
                            }
                        }

                        return true;
                    })
                    .collect(Collectors.toCollection(HashSet::new));

            changedDefEquations.put(reachableNonTerminal, changedOptions);
        }

        cfg.setNonTerminals(reachableNonTerminals);
        cfg.setDefiningEquations(changedDefEquations);
    }

    public static void removeUselessNonTerminals(CFG cfg) {
        retainGeneratingNonTerminals(cfg);
        retainReachableNonTerminals(cfg);
    }
}
