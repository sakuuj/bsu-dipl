package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FirstKService {

    public static Map<Symbol, Set<Word>> firstK(CFG cfg, int k) {

        Set<Symbol> nonTerminals = cfg.getNonTerminals();
        Map<Symbol, Set<Word>> definingEquations = cfg.getDefiningEquations();

        Map<Symbol, Set<Word>> currResult = new HashMap<>();
        Map<Symbol, Set<Word>> prevResult = new HashMap<>();
        for (Symbol nt : nonTerminals) {
            currResult.put(nt, new HashSet<>());
            prevResult.put(nt, new HashSet<>());
        }

        while (true) {
            for (Symbol nt : nonTerminals) {
                iterateOnNonTerminal(nt, nonTerminals, definingEquations, currResult, prevResult, k);
            }

            if (prevResult.equals(currResult)) {
                break;
            }

            for (Symbol nt : nonTerminals) {
                prevResult.get(nt).addAll(currResult.get(nt));
            }

        }

        return currResult;
    }

    private static void iterateOnNonTerminal(Symbol nonTerminal,
                                             Set<Symbol> nonTerminals,
                                             Map<Symbol, Set<Word>> defEquations,
                                             Map<Symbol, Set<Word>> currResult,
                                             Map<Symbol, Set<Word>> prevResult,
                                             int k) {

        Set<Word> defEquation = defEquations.get(nonTerminal);

        for (Word option : defEquation) {

            if (option.length() == 1 && option.getAt(0).equals(nonTerminal)) {
                continue;
            }

            boolean hasConcatWithEmptySet = false;

            for (Symbol symbol : option) {

                if (!nonTerminals.contains(symbol)) {
                    continue;
                }

                if (currResult.get(symbol).isEmpty()) {
                    hasConcatWithEmptySet = true;
                    break;
                }
            }

            if (hasConcatWithEmptySet) {
                continue;
            }

            Word derivation = new Word();

            int size = option.size();
            for (int i = 0; i < size; i++) {

                Symbol symbol = option.getAt(i);

                if (!nonTerminals.contains(symbol)) {
                    derivation.append(symbol);

                    if (derivation.length() == k || i == size - 1) {
                       currResult.get(nonTerminal).add(derivation);
                       break;
                    }
                    continue;
                }

                iterateOnNonTerminalRecursive(
                        nonTerminal,
                        nonTerminals,
                        currResult,
                        prevResult,
                        k,
                        option,
                        derivation,
                        symbol,
                        i
                );

            }
        }

    }


    private static void iterateOnNonTerminalRecursive(Symbol nonTerminal,
                                                      Set<Symbol> nonTerminals,
                                                      Map<Symbol, Set<Word>> currResult,
                                                      Map<Symbol, Set<Word>> prevResult,
                                                      int k,
                                                      Word currentOption,
                                                      Word currentlyDerived,
                                                      Symbol currentNonTerminal,
                                                      int currentSymbolIndex) {

        Set<Word> availableDerivations = prevResult.get(currentNonTerminal);

        for (Word ad : availableDerivations) {

            Word derivation = new Word(currentlyDerived);

            derivation.concat(ad);

            if (derivation.length() >= k) {
                currResult.get(nonTerminal).add(derivation.subWord(0, k));
                continue;
            }

            int size = currentOption.size();
            if (currentSymbolIndex + 1 == size) {
                currResult.get(nonTerminal).add(derivation);
                continue;
            }

            for (int i = currentSymbolIndex + 1; i < size; i++) {

                Symbol symbol = currentOption.getAt(i);

                if (!nonTerminals.contains(symbol)) {
                    derivation.append(symbol);

                    if (derivation.length() == k || i == size - 1) {
                        currResult.get(nonTerminal).add(derivation);
                    }
                    continue;
                }

                iterateOnNonTerminalRecursive(nonTerminal,
                        nonTerminals,
                        currResult,
                        prevResult,
                        k,
                        currentOption,
                        derivation,
                        symbol,
                        i);

            }
        }
    }
}
