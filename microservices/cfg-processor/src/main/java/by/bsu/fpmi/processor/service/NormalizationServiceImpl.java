package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Service
public class NormalizationServiceImpl implements NormalizationService {

    @Override
    public void retainGeneratingNonTerminals(CFG cfg) {

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

                    boolean allSymbolsAreGenerating = true;

                    for (Symbol symbol : option) {

                        if (!generatingNonTerminals.contains(symbol) && !terminals.contains(symbol)) {
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

                    if (!generatingNonTerminals.contains(symbol) && !terminals.contains(symbol)) {
                        optionsToRemove.add(word);
                        break;
                    }
                }
            }

            changedOptions.removeAll(optionsToRemove);
            changedDefEquations.put(generatingNonTerminal, changedOptions);
        }

        cfg.setNonTerminals(generatingNonTerminals);
        cfg.setDefiningEquations(changedDefEquations);
    }

    @Override
    public void retainReachableNonTerminals(CFG cfg) {

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

        cfg.setNonTerminals(reachableNonTerminals);
        cfg.setDefiningEquations(reachableDefEquations);
    }
}
